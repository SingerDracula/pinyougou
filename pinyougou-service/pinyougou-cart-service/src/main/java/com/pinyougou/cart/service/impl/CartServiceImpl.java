package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.Cart;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.pojo.Item;
import com.pinyougou.pojo.OrderItem;
import com.pinyougou.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service(interfaceName = "com.pinyougou.service.CartService")
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private ItemMapper itemMapper;
    @Override
    public List<Cart> addCart(List<Cart> carts, Long itemId, Integer num) {

        Item item = itemMapper.selectByPrimaryKey(itemId);
        Cart cart = searchCartsSellerId(carts, item.getSellerId());
        if(cart == null){
            Cart newCart = new Cart();
            newCart.setSellerId(item.getSellerId());
            newCart.setSellerName(item.getSeller());
            OrderItem orderItem = createOrderItem(item, num);
            ArrayList<OrderItem> list = new ArrayList<>();
            list.add(orderItem);
            newCart.setOrderItems(list);
            carts.add(newCart);
            System.out.println(carts);
        }else {
            OrderItem orderItem = searchOrderItemByItemId(cart, itemId);
            if(orderItem == null){
                OrderItem newOrderItem = createOrderItem(item, num);
                cart.getOrderItems().add(newOrderItem);
            }else {
                orderItem.setNum(orderItem.getNum()+num);
                orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()*orderItem.getNum()));
                if(orderItem.getNum() <= 0){
                    cart.getOrderItems().remove(orderItem);
                }
                if(cart.getOrderItems().size() == 0){
                    carts.remove(cart);
                }
            }
        }
        return carts;
    }

    @Override
    public List<Cart> findCartRedis(String username) {
        List<Cart> carts = (List<Cart>) redisTemplate.boundValueOps("cart_" + username).get();
        if (carts == null){
            carts = new ArrayList<>();
        }else{
            for (Cart cart : carts) {
                if (cart == null){
                    carts = new ArrayList<>();
                }
            }
        }

        return carts;
    }

    @Override
    public void saveCartRedis(String username, List<Cart> newCarts) {
        redisTemplate.boundValueOps("cart_"+username).set(newCarts);
    }

    @Override
    public List<Cart> mergeCart(List<Cart> cookieCart, List<Cart> cartList) {
        for (Cart cart : cookieCart) {
            if (cart == null){
                return cartList;
            }
            for (OrderItem orderItem : cart.getOrderItems()) {
                cartList = addCart(cartList, orderItem.getItemId(), orderItem.getNum());
            }
        }
        return cartList;
    }

    Cart searchCartsSellerId(List<Cart> carts,String sellerId){
            for (Cart cart : carts) {
                if (cart == null){
                    return null;
                }
                if(cart.getSellerId().equals(sellerId)){
                    return cart;
                }
            }


        return null;
    }

    OrderItem createOrderItem(Item item,Integer num){
        OrderItem orderItem = new OrderItem();
        orderItem.setItemId(item.getId());
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setSellerId(item.getSellerId());
        orderItem.setNum(num);
        orderItem.setPrice(item.getPrice());
        orderItem.setTitle(item.getTitle());
        orderItem.setPicPath(item.getImage());
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()*num));
        return orderItem;
    }

    OrderItem searchOrderItemByItemId(Cart cart,Long itemId){
        List<OrderItem> orderItems = cart.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            if(orderItem.getItemId().equals(itemId)){
                return orderItem;
            }
        }
        return null;
    }
}
