package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Address;
import com.pinyougou.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@RestController
@RequestMapping("/address")
public class AddressController {
    @Reference(timeout = 10000)
    private AddressService addressService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/findAddress")
    public List<Address> findAddress(){
        String username = request.getRemoteUser();
        System.out.println(addressService.findAll(username));
        return addressService.findAll(username);
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

    @RequestMapping("/update")
    public Boolean delete(@RequestBody Address address){
        try{
            addressService.update(address);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/save")
    public Boolean save(@RequestBody Address address){
        try{
            String username = request.getRemoteUser();
            address.setUserId(username);
            addressService.save(address);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
