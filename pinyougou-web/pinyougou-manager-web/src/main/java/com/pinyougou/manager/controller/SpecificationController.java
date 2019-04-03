package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.SpecificationOption;
import com.pinyougou.service.SpecificationOptionService;
import com.pinyougou.service.SpecificationService;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/specification")
public class SpecificationController {
    @Reference(timeout = 10000)
    private SpecificationService specificationService;

    @GetMapping("/findByPage")
    public PageResult findByPage(Specification specification,Integer page,Integer rows){
        if(specification != null && StringUtil.isNotEmpty(specification.getSpecName())){
            try {
                specification.setSpecName(new String(specification.getSpecName().getBytes("ISO8859-1"),"utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return specificationService.findByPage(specification,page,rows);
    }

    @PostMapping("/save")
    public boolean save(@RequestBody Specification specification){
        try{
            specificationService.save(specification);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/findById")
    public List<SpecificationOption> findById(Long id){
        return  specificationService.findOptions(id);
    }

    @PostMapping("/update")
    public Boolean update(@RequestBody Specification specification){
        try{
            specificationService.update(specification);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/delete")
    public Boolean delete(Long[] ids){
        try{
            specificationService.deleteAll(ids);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
