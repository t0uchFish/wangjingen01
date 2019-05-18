package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;

import java.util.List;

public interface GoodsService {
    /**
     * 分页查询商品
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    PageResult<SpuBo> querySpuByPageAndSort(String key, Boolean saleable, Integer page, Integer rows);

    /**
     * 保存商品信息
     * @param spuBo
     */
    void saveGoods(SpuBo spuBo);

    /**
     * 根据spuId查询spuDetail
     * @param id
     * @return
     */
    SpuDetail querySpuDetailById(Long id);

    /**
     * 根据spuId查询sku
     * @param id
     * @return
     */
    List<Sku> querySkuBySpuId(Long id);

    /**
     * 修改商品
     * @param spuBo
     */
    void updateGoods(SpuBo spuBo);

    /**
     * 上下架商品
     * @param id
     * @param saleable
     */
    void isSaleableGoods(Long id, Boolean saleable);

    /**
     *根据id逻辑删除shanp
     * @param id
     */
    void deleteGoodsBySouId(Long id);

    /**
     * 根据spuId查询spu
     * @param id
     * @return
     */
    Spu querySpuById(Long id);
}
