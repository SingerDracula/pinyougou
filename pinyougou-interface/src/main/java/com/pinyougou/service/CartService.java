package com.pinyougou.service;

import com.pinyougou.cart.Cart;

import java.util.List;

public interface CartService {
    List<Cart> addCart(List<Cart> carts,Long itemId,Integer num);

    List<Cart> findCartRedis(String username);

    void saveCartRedis(String username, List<Cart> newCarts);

    List<Cart> mergeCart(List<Cart> cookieCart, List<Cart> cartList);
}
