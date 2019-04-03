package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.ItemCat;
import com.pinyougou.pojo.TypeTemplate;
import com.pinyougou.service.ItemCatService;
import com.pinyougou.service.TypeTemplateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {
    @Reference(timeout = 10000)
    private ItemCatService itemCatService;
    @Reference(timeout = 10000)
    private TypeTemplateService typeTemplateService;

    @GetMapping("/finaItemCatByParentId")
    public List<ItemCat> finaItemCatByParentId(Long parentId){
        return itemCatService.findAll(parentId);
    }

    @GetMapping("/findAllTypeTemplate")
    public List<Map<String,Object>> findAllTypeTemplate(){
        return typeTemplateService.findUserByItemCat();
    }

    @PostMapping("/save")
    public Boolean save(@RequestBody ItemCat itemCat){
        try{
            itemCatService.save(itemCat);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/findTypeOneById")
    public Map<String,Object> findTypeOneById(Long id){
        return typeTemplateService.findOne(id);
    }

    @PostMapping("/update")
    public Boolean update(@RequestBody ItemCat itemCat){
        try {
            itemCatService.update(itemCat);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/delete")
    public Boolean delete(Long[] ids){
        System.out.println(ids);
        try{
            itemCatService.deleteAll(ids);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
