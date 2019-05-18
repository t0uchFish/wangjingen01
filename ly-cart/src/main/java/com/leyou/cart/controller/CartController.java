package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 添加商品到购物车
     *
     * @param cart
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {
        this.cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 查询购物车
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCart() {
        List<Cart> carts = this.cartService.queryCart();
        if (!CollectionUtils.isEmpty(carts)) {
            return ResponseEntity.ok(carts);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 修改num
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(
            @RequestParam("skuId") String skuId,
            @RequestParam("changeNum")Boolean num) {

        cartService.updateNum(skuId, num);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    /**
     * 根据skuID删除购物车
     * @param skuId
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteCartBySkuId(@RequestParam("skuId")String skuId){
        cartService.deleteCartBySkuId(skuId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 将LocalStorage的信息提添加后台
     * @param carts
     * @return
     */
    @PostMapping("list")
    public ResponseEntity<Void> addStore(@RequestBody List<Cart> carts) {
        cartService.addStore(carts);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
