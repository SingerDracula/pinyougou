package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.TypeTemplate;
import com.pinyougou.service.TypeTemplateService;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {
    @Reference(timeout = 10000)
    private TypeTemplateService typeTemplateService;

    @GetMapping("/findByPage")
    public PageResult findByPage(TypeTemplate typeTemplate,Integer page,Integer rows){
        if(typeTemplate != null && StringUtil.isNotEmpty(typeTemplate.getName())){
            try {
                typeTemplate.setName(new String(typeTemplate.getName().getBytes("ISO8859-1"),"utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return typeTemplateService.findByPage(typeTemplate, page, rows);
    }

    @GetMapping("/findBrandList")
    public List<Map<String,Object>> findBrandList(){
        return typeTemplateService.findAllBrand();
    }

    @GetMapping("/findSpecList")
    public List<Map<String,Object>> findSpecList(){
        return typeTemplateService.findSpecList();
    }

    @PostMapping("/save")
    public Boolean save(@RequestBody TypeTemplate typeTemplate){
        try {
            typeTemplateService.save(typeTemplate);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @PostMapping("/update")
    public Boolean update(@RequestBody TypeTemplate typeTemplate){
        try {
            typeTemplateService.update(typeTemplate);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/delete")
    public Boolean delete(Long[] ids){
        try {
            typeTemplateService.deleteAll(ids);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
