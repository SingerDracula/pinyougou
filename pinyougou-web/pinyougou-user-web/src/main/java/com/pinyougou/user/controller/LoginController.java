package com.pinyougou.user.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/user")
public class LoginController {
    @RequestMapping("/login")
    public String login(HttpServletRequest request){
        HashMap<String,String> map = new HashMap<>();
        /*String username = SecurityContextHolder.getContext().getAuthentication().getName();*/
        String username = request.getRemoteUser();
        return username;
    }
}
