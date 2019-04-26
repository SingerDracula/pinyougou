package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.service.OrderService;
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


    @GetMapping("/showOrder")
    public List<Map<String, Object>> showOrder(HttpServletRequest request){
        String username = request.getRemoteUser();
        System.out.println(orderService.findMyOrder(username));
        return orderService.findMyOrder(username);
    }
}
