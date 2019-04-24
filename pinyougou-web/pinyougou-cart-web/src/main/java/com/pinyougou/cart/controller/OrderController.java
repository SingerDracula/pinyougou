package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.utils.IdWorker;
import com.pinyougou.pojo.Order;
import com.pinyougou.pojo.PayLog;
import com.pinyougou.service.OrderService;
import com.pinyougou.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference(timeout = 10000)
    private OrderService orderService;
    @Reference(timeout = 10000)
    private WeixinPayService weixinPayService;
    @Autowired
    private HttpServletRequest request;
    @RequestMapping("/save")
    public Boolean save (@RequestBody Order order){
        try{
            String user = request.getRemoteUser();
            order.setUserId(user);
            order.setSourceType("2");
            orderService.save(order);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @RequestMapping("/getPayCode")
    public Map<String, String> getPayCode (HttpServletRequest request){
        String user = request.getRemoteUser();
        PayLog payLog = orderService.getPayLogByRedis(user);
        return weixinPayService.getPayCode(payLog.getOutTradeNo(), String.valueOf(payLog.getTotalFee()));
    }

    @RequestMapping("/getPayStatus")
    public Map<String, Integer> getPayStatus (String outTradeNo){
        HashMap<String, Integer> resultMap = new HashMap<>();
        resultMap.put("status", 3);
        Map<String, String> map = weixinPayService.getPayStatus(outTradeNo);
        if (map != null && map.size() > 0) {
            if ("SUCCESS".equals(map.get("trade_state"))) {
                orderService.updateStatus(outTradeNo,map.get("transaction_id"));
                resultMap.put("status", 1);
            }
            if ("NOTPAY".equals(map.get("trade_state"))) {
                resultMap.put("status", 2);
            }
        }
        return resultMap;
    }


}
