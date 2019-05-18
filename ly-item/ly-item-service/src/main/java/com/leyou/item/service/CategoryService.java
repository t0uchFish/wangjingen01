package com.leyou.item.service;

import com.leyou.item.pojo.Category;

import java.util.List;

public interface CategoryService {
    /**
     * 根据父id查询分类
     * @param pid
     * @return
     */
    List<Category> queryByParentId(Long pid);

    /**
     * 根据品牌id查寻分类
     * @param bid
     * @return
     */
    List<Category> queryByBrandId(long bid);

    /**
     * 商品查询调用分类业务
     * @param asList
     * @return
     */
    List<String> queryNamesByIds(List<Long> asList);

    /**
     * 根据分类di查询分类信息
     * @param cids
     * @return
     */
    List<Category> queryCategoryByIds(List<Long> cids);

    /**
     * 根据3级分类id，查询1~3级的分类
     * @param id
     * @return
     */
    List<Category> queryAllByCid3(Long id);

    /**
     * 根据顶级查询下级分类跟下下级分类
     * @param pid
     * @return
     */
    List<Category> queryCategoryAndSun(Long pid);
}
