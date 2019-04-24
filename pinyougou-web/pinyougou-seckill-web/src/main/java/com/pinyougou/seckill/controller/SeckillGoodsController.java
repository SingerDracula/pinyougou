package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.SeckillGoods;
import com.pinyougou.service.SeckillGoodsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {
    @Reference(timeout = 10000)
    private SeckillGoodsService seckillGoodsService;
    @RequestMapping("/findSeckillGoods")
    public List<SeckillGoods> findSeckillGoods(){
        return seckillGoodsService.findAll();
    }

    @RequestMapping("/loadSeckillgoods")
    public SeckillGoods loadSeckillgoods(Long id){
        return seckillGoodsService.findOne(id);
    }
}
