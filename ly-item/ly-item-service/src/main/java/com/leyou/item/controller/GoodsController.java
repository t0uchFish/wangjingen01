package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 分页查询商品
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable
    ) {
        PageResult<SpuBo> spus = this.goodsService.querySpuByPageAndSort(key, saleable, page, rows);

        if (spus != null && spus.getItems().size() > 0 && spus.getItems() != null) {
            return ResponseEntity.ok(spus);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 保存商品信息
     *
     * @param spuBo
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo) {
        this.goodsService.saveGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据spuId查询spuDetail
     * @param id
     * @return
     */
    @GetMapping("spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id") Long id) {
        SpuDetail spuDetail = this.goodsService.querySpuDetailById(id);
        if (spuDetail != null) {
            return ResponseEntity.ok(spuDetail);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * 根据spuID查询sku
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long id) {
        List<Sku> skus = goodsService.querySkuBySpuId(id);
        if (skus!= null && skus.size() > 0) {
            return ResponseEntity.ok(skus);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 更新商品信息
     * @param spuBo
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo) {
        this.goodsService.updateGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 上下架商品
     * @param id
     * @param saleable
     * @return
     */
    @PutMapping("goods/{id}/{saleable}")
    public ResponseEntity<Void> isSaleableGoods(
            @PathVariable("id")Long id,
            @PathVariable("saleable") Boolean saleable
            ){
        goodsService.isSaleableGoods(id,saleable);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据id逻辑删除商品
     * @param id
     * @return
     */
    @DeleteMapping("goods/{id}")
    public ResponseEntity<Void> deleteGoods(@PathVariable Long id){
        goodsService.deleteGoodsBySouId(id);
        return ResponseEntity.ok().build();
    }



    /**
     * 根据spuId查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id) {
        Spu spu = this.goodsService.querySpuById(id);
        if (spu != null) {
            return ResponseEntity.ok(spu);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
