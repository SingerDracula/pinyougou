package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/user")
public class LoginController {
    @Reference(timeout = 10000)
    private UserService userService;
    @RequestMapping("/login")
    public User login(HttpServletRequest request){
        /*String username = SecurityContextHolder.getContext().getAuthentication().getName();*/
        String username = request.getRemoteUser();
        User user = userService.showUser(username);
        return user;
    }
}
