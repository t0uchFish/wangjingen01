package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;

public interface BrandService {
    /**
     * 分页查询品牌
     * @param
     * @param
     * @return
     */
    PageResult<Brand> queryBrandByPage(int page, int rows, String search, String sortBy, Boolean descending);

    /**
     * 保存品牌信息
     * @param brand
     * @param cids
     */
    void saveBrand(Brand brand, List<Integer> cids);

    /**
     * 修改品牌
     * @param brand
     * @param cids
     */
    void updateBrand(Brand brand, List<Integer> cids);

    /**
     * 根据品牌id删除品牌
     * @param bid
     */
    void deleteBrandById(long bid);

    /**
     * 根据品牌id查询商品品牌name
     * @param brandId
     * @return
     */
    String selectByPrimaryKey(Long brandId);

    /**
     * 根据分类id查询品牌
     * @param cid
     * @return
     */
    List<Brand> queryBrandByCategorybid(Long cid);

    List<Brand> queryBrandByIds(List<Long> ids);
    /**
     * 根据品牌id查询品牌
     * @param ids
     * @return
     */
    Brand queryBrandById(Long ids);
}
