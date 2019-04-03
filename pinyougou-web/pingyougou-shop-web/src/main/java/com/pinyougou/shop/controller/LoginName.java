package com.pinyougou.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
@Controller
public class LoginName {
    @Autowired
    private AuthenticationManager authenticationManager;
    @RequestMapping("/mylogin")
    public String mylogin(String username, String password, String code, HttpServletRequest request){
        try {
            if("post".equalsIgnoreCase(request.getMethod()))
            {
                if (code.equalsIgnoreCase((String) request.getSession().getAttribute(VerifyController.VERIFY_CODE))){
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);
                    Authentication authenticate = authenticationManager.authenticate(token);
                    if(authenticate.isAuthenticated()){
                        SecurityContextHolder.getContext().setAuthentication(authenticate);
                        return "redirect:/admin/index.html";
                    }
                }
            }else {
                return "redirect:/shoplogin.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/shoplogin.html";
    }


    @ResponseBody
    @GetMapping("/getLoginName")
    public Map<String,String> getLoginName(){
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String,String> map = new HashMap();
        map.put("loginName",loginName);
        System.out.println(map);
        return map;

    }

}
