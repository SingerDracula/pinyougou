package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Address;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/safe")
public class SafeController {
    @Reference(timeout = 10000)
    private UserService userService;

    @RequestMapping("/changePassword")
    public Boolean changePassword(@RequestBody Map<String,String> user, HttpServletRequest request){
        try{
            String username = user.get("username");
            String password = user.get("confirmPassword");
            String oldPassword = user.get("oldPassword");
            if(!username.equals(request.getRemoteUser())){
                return false;
            }
            Boolean flag = userService.changePassword(username,password,oldPassword);
            if(flag){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/check")
    public Boolean check(@RequestParam(value ="vcode" ) String  vcode,@RequestParam(value ="smsCode" ) String smsCode,
                         @RequestParam(value ="phone" ) String  phone,
                         HttpServletRequest request){
        try{

            if(!vcode.equals(request.getSession().getAttribute(VerifyController.VERIFY_CODE))){
                return  false;
            }
            User user = userService.showUser(request.getRemoteUser());
            return userService.checkCode(phone, smsCode);

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/changePhone")
    public Boolean changePhone(@RequestParam(value ="phone" )String phone,HttpServletRequest request){
        try{
            userService.changePhone(phone,request.getRemoteUser());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
