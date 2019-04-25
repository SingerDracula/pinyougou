package com.pinyougou.user.controller;

import com.pinyougou.pojo.Address;
import com.pinyougou.pojo.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/address")
public class addressController {

    @PostMapping("/findAddress")
    public Address findAddress(){
        return null;
    }
}
