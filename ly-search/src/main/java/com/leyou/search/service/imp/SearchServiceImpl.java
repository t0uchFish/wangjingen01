package com.leyou.search.service.imp;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.SpecParam;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import com.leyou.search.utils.SearchRequest;
import com.leyou.search.utils.SerarchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.AggregationPhase;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecificationClient specificationClient;


    @Override
    public PageResult<Goods> queryGoodsByPage(SearchRequest searchRequest) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        String key = searchRequest.getKey();
        if (!StringUtils.isNotBlank(key)) {
            return null;
        }
//        queryBuilder.withQuery(QueryBuilders.termQuery("all", key));
        QueryBuilder query = buildBasicQueryWithFilter(searchRequest);
        queryBuilder.withQuery(query);

        queryBuilder.withPageable(PageRequest.of(searchRequest.getPage() - 1, searchRequest.getSize()));
        //排序
        String sortBy = searchRequest.getSortBy();
        Boolean descending = searchRequest.getDescending();
        if (StringUtils.isNotBlank(sortBy)) {
            queryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(descending ? SortOrder.DESC : SortOrder.ASC));
        }
        //聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("brandIdName").field("brandId"));
        queryBuilder.addAggregation(AggregationBuilders.terms("cidName").field("cid3"));
        AggregatedPage<Goods> goods = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());
        List<Category> categories = queryCategoryByAgg(goods);
        List<Brand> brands = queryBrandsByAgg(goods);

        List<Map<String, Object>> specs = null;
        if (categories.size() == 1) {
           specs = getSpecs(categories.get(0).getId(),query);

        }
        return new SerarchResult(goods.getTotalElements(), new Long(goods.getTotalPages()), goods.getContent(), categories, brands, specs);
    }

    // 构建基本查询条件
    private QueryBuilder buildBasicQueryWithFilter(SearchRequest request) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 基本查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));
        // 过滤条件构建器
        BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
        // 整理过滤条件
        Map<String, String> filter = request.getFilter();
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // 商品分类和品牌可以直接聚合不需要拼接
            if (key != "cid3" && key != "brandId") {
                key = "specs." + key + ".keyword";
            }
            // 字符串类型，进行term查询
            filterQueryBuilder.must(QueryBuilders.termQuery(key, value));
        }
        // 添加过滤条件
        queryBuilder.filter(filterQueryBuilder);
        return queryBuilder;
    }

    private List<Map<String, Object>> getSpecs(Long id,QueryBuilder query) {
        List<Map<String, Object>> specs = new ArrayList<>();

        //查询分类的规格参数
        List<SpecParam> params = specificationClient.querySpecParam(null, id, true,null);
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(query);
        params.forEach(
                param -> {
                    String name = param.getName();
                    queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
                }
        );
        AggregatedPage<Goods> search = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());
        params.forEach(param -> {
            String name = param.getName();
            StringTerms aggregations = (StringTerms) search.getAggregation(name);
            List<StringTerms.Bucket> buckets = aggregations.getBuckets();
            ArrayList<String> list = new ArrayList<>();
            for (StringTerms.Bucket bucket : buckets) {
                list.add(bucket.getKeyAsString());
            }

            HashMap<String, Object> spec = new HashMap<>();
            spec.put("k", name);
            spec.put("options", list);
            specs.add(spec);
        });
        return specs;
    }

    private List<Category> queryCategoryByAgg(AggregatedPage<Goods> goods) {
        LongTerms cidBuckets = (LongTerms) goods.getAggregation("cidName");
        List<LongTerms.Bucket> buckets = cidBuckets.getBuckets();
        ArrayList<Long> cids = new ArrayList<>();
        buckets.forEach(bucket -> {
            cids.add(bucket.getKeyAsNumber().longValue());
        });
        List<Category> categories = categoryClient.queryCategoryByIds(cids);
        return categories;
    }


    private List<Brand> queryBrandsByAgg(AggregatedPage<Goods> goods) {
        LongTerms brandBuckets = (LongTerms) goods.getAggregation("brandIdName");
        List<LongTerms.Bucket> buckets = brandBuckets.getBuckets();
        ArrayList<Long> brandIds = new ArrayList<>();
        buckets.forEach(bucket -> {
            brandIds.add(bucket.getKeyAsNumber().longValue());
        });
        List<Brand> brands = brandClient.queryBrandByIds(brandIds);
        return brands;
    }


}
