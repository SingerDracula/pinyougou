package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.service.TypeTemplateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {
    @Reference(timeout = 10000)
    private TypeTemplateService typeTemplateService;

    @RequestMapping("/findBrandIdsByTemplateId")
    public com.pinyougou.pojo.TypeTemplate findBrandIdsByTemplateId(Long id){
       return typeTemplateService.findBrandIdsByTemplateId(id);
    }

    @GetMapping("/findSpecByTemplateId")
    public List<Map> findSpecByTemplateId(Long id){
        return typeTemplateService.findSpecByTemplateId(id);
    }
}
