package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Address;
import com.pinyougou.pojo.User;
import com.pinyougou.service.AddressService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/address")
public class addressController {
    @Reference(timeout = 10000)
    private AddressService addressService;

    @PostMapping("/findAddress")
    public List<Address> findAddress(){
        return addressService.findAddress();
    }
}
