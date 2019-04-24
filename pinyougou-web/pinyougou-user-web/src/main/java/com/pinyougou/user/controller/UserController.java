package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference(timeout = 10000)
    private UserService userService;
    @PostMapping("/register")
    public Boolean register(@RequestBody User user,String code){
        try{
            if(userService.checkCode(user.getPhone(),code)){
                userService.save(user);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/sendCode")
   public Boolean sendCode(String phone){
        try{
            if(StringUtils.isNoneBlank(phone)){
                userService.sendCode(phone);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
