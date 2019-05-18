package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 分页查询品牌
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "rows", defaultValue = "10") int rows,
            @RequestParam(value = "search",required = false) String search,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "descending",required = false) Boolean descending
    ) {
        PageResult<Brand> pageResult = brandService.queryBrandByPage(page, rows,search,sortBy,descending);
        if (pageResult != null && pageResult.getItems() != null && pageResult.getTotal() > 0) {
            return ResponseEntity.ok(pageResult);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 保存品牌信息
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids")List<Integer> cids){
        this.brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改品牌
     * @param brand
     * @param cids
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids")List<Integer> cids){
        this.brandService.updateBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据品牌id删除品牌
     * @param bid
     * @return
     */
    @DeleteMapping("bid/{bid}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bid") long bid){
        this.brandService.deleteBrandById(bid);
        return ResponseEntity.ok().build();
    }


    /**
     * 根据分类id查询品牌
     * @param cid
     * @return
     */
//    /item/brand/cid/"
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCategory(@PathVariable("cid")Long cid){
        List<Brand> brands =  brandService.queryBrandByCategorybid(cid);
        if (brands != null && brands.size() > 0) {
            return ResponseEntity.ok(brands);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * 根据多个id查询品牌
     * @param ids
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids") List<Long> ids){
        List<Brand> list = this.brandService.queryBrandByIds(ids);
        if(list == null){
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }


    /**
     * 根据品牌id查询品牌
     * @param id
     * @return
     */
    @GetMapping("bid")
    public ResponseEntity<Brand> queryBrandById(@RequestParam("id") Long id){
        Brand brand = this.brandService.queryBrandById(id);
        if(brand == null){
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(brand);
    }
}
