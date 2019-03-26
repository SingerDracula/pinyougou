package com.pinyougou.manager.controller;

import com.pinyougou.pojo.Brand;
import com.pinyougou.service.BrandService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.alibaba.dubbo.config.annotation.Reference;

@RestController
public class BrandController {

    @Reference(timeout=10000)
    private BrandService brandService;

    @GetMapping("/brand/findAll")
    public List<Brand> findAll(){
        return brandService.findAll();
    }
}
