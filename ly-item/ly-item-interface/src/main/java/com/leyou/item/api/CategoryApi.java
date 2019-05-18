package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryApi {
    @GetMapping("category/names")
    List<String> queryNamesByIds(@RequestParam("cids") List<Long> cids);

    /**
     * 根据分类id查询分类名称
     *
     * @param cids
     * @return
     */
    @GetMapping("category/cid")
    List<Category> queryCategoryByIds(@RequestParam("cids") List<Long> cids);


    /**
     * 根据3级分类id，查询1~3级的分类
     *
     * @param id
     * @return
     */
    @GetMapping("category/all/level")
    List<Category> queryAllByCid3(@RequestParam("pid") Long id);

}
