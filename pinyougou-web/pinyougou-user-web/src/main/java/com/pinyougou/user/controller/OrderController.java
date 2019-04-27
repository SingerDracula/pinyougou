package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.PayLog;
import com.pinyougou.service.OrderService;
import com.pinyougou.service.WeixinPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference(timeout = 10000)
    private OrderService orderService;
    @Reference(timeout = 10000)
    private WeixinPayService weixinPayService;


    @GetMapping("/showOrder")
    public List<Map<String, Object>> showOrder(HttpServletRequest request){
        String username = request.getRemoteUser();
        System.out.println(orderService.findMyOrder(username));
        return orderService.findMyOrder(username);
    }

    @GetMapping("/pay")
    public Map<String, String> pay(String orderId,Long totalMoney,HttpServletRequest request){
        String username = request.getRemoteUser();
        orderService.createPayLog(username,totalMoney,orderId);
        PayLog payLog = orderService.getPayLogByRedis(username);
        return weixinPayService.getPayCode(payLog.getOutTradeNo(), String.valueOf(payLog.getTotalFee()));
    }
}
