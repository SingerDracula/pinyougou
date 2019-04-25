package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Address;
import com.pinyougou.service.AddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/address")
public class addressController {
    @Reference(timeout = 10000)
    private AddressService addressService;

    @GetMapping("/findAddress")
    public List<Address> findAddress(String loginName){
        return addressService.findAddress(loginName);
    }
}
