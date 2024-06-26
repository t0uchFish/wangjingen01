package com.leyou.item.api;

import com.leyou.item.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BrandApi {

    /**
     * 根据多个id查询品牌
     *
     * @param ids
     * @return
     */
    @GetMapping("brand/list")
    List<Brand> queryBrandByIds(@RequestParam("ids") List<Long> ids);


    /**
     * 根据品牌id查询品牌
     * @param id
     * @return
     */
    @GetMapping("brand/bid")
    Brand queryBrandById(@RequestParam("id") Long id);
}
