package com.leyou.search.service.imp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.*;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.Oneway;
import java.util.*;

@Service
public class IndexService {
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    public Goods buildGoods(SpuBo spuBo) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(spuBo, goods);
        List<String> names = categoryClient.queryNamesByIds(Arrays.asList(goods.getCid1(), goods.getCid2(), goods.getCid3()));
        String all = spuBo.getTitle() + " " + StringUtils.join(names, " ") + " " + spuBo.getBname();
        goods.setAll(all);   // 所有需要被搜索的信息，包含标题，分类，甚至品牌

        ArrayList<Long> prices = new ArrayList<>();
        List<Sku> skus = goodsClient.querySkuBySpuId(goods.getId());
        HashMap<String, Object> skuMap = new HashMap<>();
        ArrayList<Map<String, Object>> skusMap = new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("image", StringUtils.isBlank(sku.getImages()) ? "" : sku.getImages().split(",")[0]);
            map.put("price", sku.getPrice());
            skusMap.add(map);
        });
        goods.setSkus(JsonUtils.serialize(skusMap)); //sku id  ,标题，图片，价格
        goods.setPrice(prices);//价格

        //查询spuDetail
        SpuDetail spuDetail = goodsClient.querySpuDetailById(spuBo.getId());
        //查询sprc_param
        List<SpecParam> params = specificationClient.querySpecParam(null, spuBo.getCid3(),null,null);
        //获取通用规格参数
        Map<Long, String> genericSpecMap = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
        //获取特有规格参数
        Map<Long, List<String>> specialSpecMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });

        HashMap<String, Object> specs = new HashMap<>();
        for (SpecParam param : params) {
            Long paramId = param.getId();
            String name = param.getName();
            Object value = null;
            if (param.getGeneric()) {
                value = genericSpecMap.get(paramId);
                if (param.getNumeric()) {
                    //数值类型需要加分段
                    value = this.chooseSegment(value.toString(), param);
                }
            } else {
                value = specialSpecMap.get(paramId);
            }
            specs.put(name, value);
            if (null == value) {
                value = "其他";
            }
        }


        goods.setSpecs(specs);// 可搜索的规格参数，key是参数名，值是参数值

        return goods;
    }


    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public void createIndex(Long id){
        Spu spu = this.goodsClient.querySpuById(id);
        SpuBo spuBo = new SpuBo();
        BeanUtils.copyProperties(spu,spuBo);
        Goods goods = this.buildGoods(spuBo);
        this.goodsRepository.save(goods);
    }

    public void deleteIndex(Long id) {
        this.goodsRepository.deleteById(id);
    }


}
