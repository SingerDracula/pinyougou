package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.Cart;
import com.pinyougou.common.utils.CookieUtils;
import com.pinyougou.service.CartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
@RestController
@RequestMapping("/cart")
public class cartController {
    @Reference(timeout = 10000)
    private CartService cartService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/addCart")
    @CrossOrigin(origins = "http://item.pinyougou.com",allowCredentials = "true")
    public Boolean addCart(Long itemId,Integer num){
        try {

            List<Cart> carts = findCart();
            List<Cart> newCarts = cartService.addCart(carts, itemId, num);
            String username = request.getRemoteUser();
            if(StringUtils.isNoneBlank(username)){
                cartService.saveCartRedis(username,newCarts);
            }else {
                CookieUtils.setCookie(request,response,CookieUtils.CookieName.PINYOUGOU_CART,JSON.toJSONString(newCarts),
                        3600*24,true);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @RequestMapping("/findCart")
    public List<Cart> findCart(){
        String username = request.getRemoteUser();
        List<Cart> cartList = null;
        if(StringUtils.isNoneBlank(username)){
            cartList = cartService.findCartRedis(username);
            String carts = CookieUtils.getCookieValue(request, CookieUtils.CookieName.PINYOUGOU_CART, true);
            if(StringUtils.isNoneBlank(carts)){
                List<Cart>  cookieCart = JSON.parseArray(carts, Cart.class);
                if(cookieCart != null && cookieCart.size()>0){
                    cartList = cartService.mergeCart(cookieCart,cartList);
                    cartService.saveCartRedis(username,cartList);
                    CookieUtils.deleteCookie(request,response,CookieUtils.CookieName.PINYOUGOU_CART);
                }
            }

        }else {
            String carts = CookieUtils.getCookieValue(request, CookieUtils.CookieName.PINYOUGOU_CART, true);
            if(StringUtils.isBlank(carts)){
                carts = "[]";
            }
            cartList = JSON.parseArray(carts, Cart.class);
        }
        System.out.println(cartList);
        return cartList;
    }
}
