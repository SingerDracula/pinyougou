package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.util.StringUtil;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Reference(timeout = 10000)
    private SellerService sellerService;

    @GetMapping("/findByPage")
    public PageResult findByPage(Integer page, Integer rows, Seller seller){
            try {
                if(seller != null && StringUtil.isNotEmpty(seller.getName())) {
                    seller.setName(new String(seller.getName().getBytes("ISO8859-1"), "utf-8"));
                }
                if (seller != null && StringUtil.isNotEmpty(seller.getNickName())){
                    seller.setNickName(new String(seller.getNickName().getBytes("ISO8859-1"), "utf-8"));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        return sellerService.findByPage(seller,page,rows);
    }

    @GetMapping("/updateStatus")
    public Boolean updateStatus(String sellerId,String status){
        try{
            sellerService.updateStatusBySellerId(sellerId,status);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
