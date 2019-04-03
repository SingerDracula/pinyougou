package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Goods;
import com.pinyougou.service.GoodsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Reference(timeout = 10000)
    private GoodsService goodsService;

    @PostMapping("/save")
    public Boolean save(@RequestBody Goods goods){
        try {
            goods.setSellerId(SecurityContextHolder.getContext().getAuthentication().getName());
            goodsService.save(goods);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/findByPage")
    public PageResult findByPage(int page,int rows,Goods goods){
        String sellerName = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(sellerName);
        if(goods != null && StringUtil.isNotEmpty(goods.getGoodsName())){
            try {
                goods.setGoodsName(new String(goods.getGoodsName().getBytes("ISO8859-1"),"utf-8"));
            } catch (UnsupportedEncodingException e) {


            }
        }
        System.out.println(goodsService.findByPage(goods,page,rows));
        return goodsService.findByPage(goods,page,rows);
    }
}
