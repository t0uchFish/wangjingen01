package com.leyou.search.utils;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;

import java.util.List;
import java.util.Map;

public class SerarchResult extends PageResult {
    private List<Category> category;
    private List<Brand> brand;
    private List<Map<String, Object>> specs; // 规格参数过滤条件

    public SerarchResult(Long total, Long totalPage, List items, List<Category> category, List<Brand> brand, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.category = category;
        this.brand = brand;
        this.specs = specs;
    }

    public SerarchResult() {

    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public List<Brand> getBrand() {
        return brand;
    }

    public void setBrand(List<Brand> brand) {
        this.brand = brand;
    }

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }
}
