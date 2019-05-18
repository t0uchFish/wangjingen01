package com.leyou.cart.service;


import com.leyou.cart.pojo.Cart;

import java.util.List;

public interface CartService {
    /**
     * 添加商品到购物车
     * @param cart
     */
    void addCart(Cart cart);

    /**
     * 查询购物车
     * @return
     */
    List<Cart> queryCart();

    /**
     * 修改数量
     * @param skuId
     * @param num
     */
    void updateNum(String skuId, Boolean num);

    /**
     * 根据skuID删除购物车
     * @param skuId
     */
    void deleteCartBySkuId(String skuId);

    /**
     * 将LocalStorage的信息提添加后台
     * @param carts
     * @return
     */
    void addStore(List<Cart> carts);
}
