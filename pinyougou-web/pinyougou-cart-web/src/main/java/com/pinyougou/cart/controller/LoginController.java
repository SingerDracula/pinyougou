package com.pinyougou.cart.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/user")
public class LoginController {
    @RequestMapping("/showName")
    public Map<String,String> showName(HttpServletRequest request){
        String user = request.getRemoteUser();
        Map<String,String> map = new HashMap<>();
        map.put("loginName",user);
        return map;
    }
}
