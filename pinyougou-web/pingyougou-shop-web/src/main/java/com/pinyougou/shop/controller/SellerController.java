package com.pinyougou.shop.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/shopseller")
public class SellerController {
    @Reference(timeout = 10000)
    private SellerService sellerService;

    @PostMapping("/save")
    public Boolean save(@RequestBody Seller seller){
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String newPassword = passwordEncoder.encode(seller.getPassword());
            seller.setPassword(newPassword);
            sellerService.save(seller);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
