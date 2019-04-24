package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.PayLog;
import com.pinyougou.service.SeckillOrderService;
import com.pinyougou.service.WeixinPayService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/seckillOrder")
public class SeckillOrderController {
    @Reference(timeout = 10000)
    private WeixinPayService weixinPayService;
    @Reference(timeout = 10000)
    private SeckillOrderService seckillOrderService;

    @RequestMapping("/submitOrder")
    public boolean submitOrder(Long id, HttpServletRequest request){
        try {
            String user = request.getRemoteUser();

            seckillOrderService.submitOrder(id,user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/genPayCode")
    public Map<String, String> getPayCode (HttpServletRequest request){
        String user = request.getRemoteUser();
        PayLog payLog = seckillOrderService.getPayLogByRedis(user);
        return weixinPayService.getPayCode(payLog.getOutTradeNo(), String.valueOf(payLog.getTotalFee()));
    }

    @RequestMapping("/queryPayStatus")
    public Map<String, Integer> queryPayStatus (String outTradeNo){
        HashMap<String, Integer> resultMap = new HashMap<>();
        resultMap.put("status", 3);
        Map<String, String> map = weixinPayService.getPayStatus(outTradeNo);
        System.out.println(map);
        if (map != null && map.size() > 0) {
            if ("SUCCESS".equals(map.get("trade_state"))) {
                seckillOrderService.updateStatus(outTradeNo,map.get("transaction_id"));
                resultMap.put("status", 1);
            }
            if ("NOTPAY".equals(map.get("trade_state"))) {
                resultMap.put("status", 2);
            }
        }
        return resultMap;
    }
}
