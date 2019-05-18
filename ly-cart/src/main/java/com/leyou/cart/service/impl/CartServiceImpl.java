package com.leyou.cart.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.auth.entiy.UserInfo;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import com.leyou.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    static final String KEY_PREFIX = "ly:cart:uid:";

    @Override
    public void addCart(Cart cart) {

        UserInfo user = LoginInterceptor.getUserInfo();
        cart.setUserId(user.getId());

        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(KEY_PREFIX + user.getId());

        //根据skuid获取商品信息，转成字符串
        String skuId = cart.getSkuId().toString();

        //判断商品是否已经保存
        Boolean boo = ops.hasKey(skuId);
        if (boo) {
            //以保存，更改num
            String json = ops.get(skuId).toString();
            Cart redisCart = JsonUtils.parse(json, Cart.class);
            redisCart.setNum(redisCart.getNum() + cart.getNum());
            redisCart.setUserId(user.getId());
            ops.put(redisCart.getSkuId().toString(), JsonUtils.serialize(redisCart));
        } else {
            //未保存，直接保存
            ops.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
        }

    }

    @Override
    public List<Cart> queryCart() {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String key = KEY_PREFIX + userInfo.getId();
        if (!this.redisTemplate.hasKey(key)) {
            // 不存在，直接返回
            return null;
        }
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(key);
        List<Object> carts = ops.values();
        if (CollectionUtils.isEmpty(carts)) {
            return null;
        }
        return carts.stream().map(o -> JsonUtils.parse(o.toString(), Cart.class)).collect(Collectors.toList());
    }

    @Override
    public void updateNum(String skuId, Boolean num) {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        String json = ops.get(skuId).toString();
        Cart cart = JsonUtils.nativeRead(json, new TypeReference<Cart>() {
        });

        if (num) {
            cart.setNum(cart.getNum() + 1);
        } else {
            cart.setNum(cart.getNum() - 1);
        }
        ops.put(skuId, JsonUtils.serialize(cart));
    }

    @Override
    public void deleteCartBySkuId(String skuId) {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        ops.delete(skuId);
    }

    @Override
    public void addStore(List<Cart> carts) {
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        carts.forEach(cart -> {
            cart.setUserId(userInfo.getId());
            ops.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
        });

    }


}
