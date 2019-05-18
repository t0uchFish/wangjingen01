package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父类id查询分类
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryByParentId(@RequestParam(value = "pid") Long pid) {
        List<Category> categoryList = categoryService.queryByParentId(pid);

        if (categoryList != null && categoryList.size() > 0) {
            return ResponseEntity.ok(categoryList);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 根据品牌id查询分类
     *
     * @param bid
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") long bid) {
        List<Category> categoryList = categoryService.queryByBrandId(bid);
        if (categoryList != null && categoryList.size() > 0) {
            return ResponseEntity.ok(categoryList);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 根据分类id查询分类名称
     * @param cids
     * @return
     */
    @GetMapping("names")
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("cids") List<Long> cids) {
        List<String> names = categoryService.queryNamesByIds(cids);
        if (names != null && names.size() > 1) {
            return ResponseEntity.ok(names);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据分类id查询分类
     * @param cids
     * @return
     */
    @GetMapping("cid")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("cids") List<Long> cids) {
        List<Category> categorys = categoryService.queryCategoryByIds(cids);
        if (categorys != null && categorys.size() > 0) {
            return ResponseEntity.ok(categorys);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * 根据3级分类id，查询1~3级的分类
     * @param id
     * @return
     */
    @GetMapping("all/level")
    public ResponseEntity<List<Category>> queryAllByCid3(@RequestParam("pid") Long id){
        List<Category> list = this.categoryService.queryAllByCid3(id);
        if (list == null || list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

//     ly.http.get("item/category/pid=" + pid)

    /**
     * 根据顶级查询下级分类跟下下级分类
     * @param pid
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Category>> queryCategoryAndSun(@RequestParam("pid") Long pid){
        List<Category> categories = categoryService.queryCategoryAndSun(pid);
        if (!CollectionUtils.isEmpty(categories)) {
            return ResponseEntity.ok(categories);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
