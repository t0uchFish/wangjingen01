package com.leyou.item.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.item.mapper.*;
import com.leyou.item.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    private BrandServiceImpl brandService;
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Override
    @Transactional
    public PageResult<SpuBo> querySpuByPageAndSort(String key, Boolean saleable, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        if (saleable != null) {
            criteria.orEqualTo("saleable", saleable);
        }

        criteria.andEqualTo("valid", true);

        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }

        Page<Spu> spus = (Page<Spu>) goodsMapper.selectByExample(example);
        ArrayList<SpuBo> spuBos = new ArrayList<>();

        spus.forEach(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
            List<String> cnames = categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(cnames, "/"));
            String bname = brandService.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(bname);
            spuBos.add(spuBo);
        });


        return new PageResult<SpuBo>(spus.getTotal(), new Long(spus.getPages()), spuBos);

    }

    @Override
    @Transactional
    public void saveGoods(SpuBo spu) {
        //保存spu
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spuMapper.insert(spu);
        //保存spuDetail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insert(spuDetail);
        //保存sku跟stock
        Stock stock = new Stock();
        spu.getSkus().forEach(sku -> {
            if (sku.getEnable()) {
                sku.setSpuId(spu.getId());
                sku.setCreateTime(new Date());
                sku.setLastUpdateTime(sku.getCreateTime());
                skuMapper.insert(sku);
                stock.setSkuId(sku.getId());
                stock.setStock(sku.getStock());
                stockMapper.insert(stock);
            }
        });
        this.sendMessage(spu.getId(),"insert");
    }

    @Override
    public SpuDetail querySpuDetailById(Long id) {
        return spuDetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Sku> querySkuBySpuId(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skus = skuMapper.select(sku);

        skus.forEach(sku1 -> {
            sku1.setStock(stockMapper.selectByPrimaryKey(sku1.getId()).getStock());
        });
        return skus;

    }

    @Override
    @Transactional
    public void updateGoods(SpuBo spu) {
        //保存spu
        spu.setLastUpdateTime(new Date());
        spuMapper.updateByPrimaryKey(spu);
        //保寸spuDetail
        spuDetailMapper.updateByPrimaryKey(spu.getSpuDetail());

        //查询skuId
        List<Long> skuIds = skuMapper.querySkuIdBySpuId(spu.getId());


        List<Sku> skus = spu.getSkus();
        //循环skus
        skus.forEach(sku -> {
            sku.setLastUpdateTime(new Date());
            //判断skuid是否包含sku的id
            if (skuIds.contains(sku.getId())) {
                skuMapper.updateByPrimaryKeySelective(sku);
                skuIds.remove(sku.getId());
                Stock stock = new Stock();
                stock.setSkuId(sku.getId());
                stock.setStock(sku.getStock());
                stockMapper.updateByPrimaryKeySelective(stock);
                skuIds.removeIf(
                        skuId -> skuId == sku.getId().longValue()
                );

            } else {
                sku.setCreateTime(new Date());
                sku.setLastUpdateTime(sku.getCreateTime());
                sku.setSpuId(spu.getId());
                skuMapper.insert(sku);
                Stock stock = new Stock();
                stock.setSkuId(sku.getId());
                stock.setStock(sku.getStock());
                stockMapper.insert(stock);
            }
        });

        if (skuIds.size() > 0) {
            skuIds.forEach(skuId -> {
                skuMapper.updateSkuByEnableById(skuId);
            });
        }
        this.sendMessage(spu.getId(),"update");
    }

    @Override
    public void isSaleableGoods(Long id, Boolean saleable) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setSaleable(!saleable);
        spuMapper.updateByPrimaryKeySelective(spu);

    }

    @Override
    public void deleteGoodsBySouId(Long id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setValid(false);
        spuMapper.updateByPrimaryKeySelective(spu);
        this.sendMessage(id,"delete");
    }

    @Override
    public Spu querySpuById(Long id) {
        return spuMapper.selectByPrimaryKey(id);
    }


    //发送消息方法
    private void sendMessage(Long id, String type){
        // 发送消息
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (Exception e) {
            logger.error("{}商品消息发送异常，商品id：{}", type, id, e);
        }
    }

}