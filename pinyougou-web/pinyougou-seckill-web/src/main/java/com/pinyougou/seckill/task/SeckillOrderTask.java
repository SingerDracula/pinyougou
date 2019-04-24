package com.pinyougou.seckill.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.SeckillOrder;
import com.pinyougou.service.SeckillOrderService;
import com.pinyougou.service.WeixinPayService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SeckillOrderTask {
    @Reference(timeout=10000)
    private SeckillOrderService seckillOrderService;
    @Reference(timeout = 10000)
    private WeixinPayService weixinPayService;
    @Scheduled(cron = "0/10 * * * * *")
    public void closeOrderTask(){
        List<SeckillOrder> seckillOrderList =  seckillOrderService.findTimeOutOrder();
        if(seckillOrderList.size()>0){
            for (SeckillOrder seckillOrder : seckillOrderList) {
                Map<String,String> map = weixinPayService.closeOrder(seckillOrder.getId().toString());
                if("SUCCESS".equals(map.get("result_code"))) {
                    seckillOrderService.deleteFromRedis(seckillOrder);
                }
            }
        }

        }
    }

