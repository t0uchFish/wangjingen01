package com.leyou.page.service.impl;

import com.leyou.item.pojo.*;
import com.leyou.page.clients.BrandClient;
import com.leyou.page.clients.CategotyClient;
import com.leyou.page.clients.GoodsClient;
import com.leyou.page.clients.SpecificationClient;
import com.leyou.page.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private CategotyClient categotyClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecificationClient specificationClient;

    @Override
    public Map<String, Object> loadModel(Long id) {
        //spu
        Spu spu = goodsClient.querySpuById(id);
        //spuDetail
        SpuDetail spuDetail = goodsClient.querySpuDetailById(id);
        //sku集合
        List<Sku> skus = goodsClient.querySkuBySpuId(id);

        //分类集合
        List<Category> categories = categotyClient.queryAllByCid3(spu.getCid3());
        //品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        //查询特有规格参数
        List<SpecParam> specParams = specificationClient.querySpecParam(null, spu.getCid3(), null, false);
        HashMap<Long, String> paramMap = new HashMap<>();
        specParams.forEach(specParam -> {
            paramMap.put(specParam.getId(), specParam.getName());
        });
        //查询通用规格参数
        List<SpecParam> params = specificationClient.querySpecParam(null, spu.getCid3(), null, true);
        HashMap<Long, String> genParams = new HashMap<>();
        params.forEach(specParam -> {
            genParams.put(specParam.getId(), specParam.getName());
        });

        //查询规格分类租
        List<SpecGroup> specGroups = specificationClient.querySpecGroups(spu.getCid3());



        HashMap<String, Object> map = new HashMap<>();
        map.put("spu", spu);
        map.put("spuDetail", spuDetail);
        map.put("skus", skus);
        map.put("categories", categories);
        map.put("brand", brand);
        map.put("paramMap", paramMap);
        map.put("genParams", genParams);
        map.put("specGroups", specGroups);

        return map;
    }
}
