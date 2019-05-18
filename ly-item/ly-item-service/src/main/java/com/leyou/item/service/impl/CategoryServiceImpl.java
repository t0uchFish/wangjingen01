package com.leyou.item.service.impl;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryByParentId(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        List<Category> categories = categoryMapper.select(category);
        return categories;
    }

    @Override
    public List<Category> queryByBrandId(long bid) {
        return categoryMapper.queryByBrandId(bid);
    }

    @Override
    public List<String> queryNamesByIds(List<Long> asList) {
        ArrayList<String> strings = new ArrayList<>();
        asList.forEach(cid -> {
            Category category = new Category();
            category.setId(cid);
            Category categorys = categoryMapper.selectByPrimaryKey(category);
            strings.add(categorys.getName());

        });
        return strings;

    }

    @Override
    public List<Category> queryCategoryByIds(List<Long> cids) {
        ArrayList<Category> categories = new ArrayList<>();

        cids.forEach(cid -> {
            Category category = new Category();
            category.setId(cid);
            Category categorys = categoryMapper.selectByPrimaryKey(category);
            categories.add(categorys);
        });
        return categories;
    }
    public List<Category> queryAllByCid3(Long id) {
        Category c3 = this.categoryMapper.selectByPrimaryKey(id);
        Category c2 = this.categoryMapper.selectByPrimaryKey(c3.getParentId());
        Category c1 = this.categoryMapper.selectByPrimaryKey(c2.getParentId());
        return Arrays.asList(c1,c2,c3);
    }

    @Override
    public List<Category> queryCategoryAndSun(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        List<Category> categoryList = categoryMapper.select(category);

        categoryList.forEach(cg -> {
            category.setParentId(cg.getId());
            List<Category> list = categoryMapper.select(category);
            cg.setCategories(list);
        });
        return categoryList;
    }

}