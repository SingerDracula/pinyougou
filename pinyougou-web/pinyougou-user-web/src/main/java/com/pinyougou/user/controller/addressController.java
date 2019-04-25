package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Address;
import com.pinyougou.service.AddressService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@RestController
@RequestMapping("/address")
public class addressController {
    @Reference(timeout = 10000)
    private AddressService addressService;

    @GetMapping("/findAddress")
    public List<Address> findAddress(HttpServletRequest request){
        String username = request.getRemoteUser();
        return addressService.findAddress(username);
    }

    @RequestMapping("/save")
    public Boolean save(@RequestBody Address address){
        try{
            addressService.update(address);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/delete")
    public Boolean delete(Long id){
        try{
            addressService.delete(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/modity")
    public Boolean deletmoditye(Long id){
        try{
            addressService.delmodityete(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
