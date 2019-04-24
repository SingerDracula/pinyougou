package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.ContentCategory;
import com.pinyougou.service.ContentCategoryService;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

@RestController
@RequestMapping("/contentCategory")
public class ContentCategoryController {
    @Reference(timeout = 10000)
    private ContentCategoryService contentCategoryService;
    @RequestMapping("/findByPage")
    public PageResult findByPage(int page,int rows){
        return contentCategoryService.findByPage(page,rows);
    }

    @RequestMapping("/save")
    public Boolean save(@RequestBody ContentCategory contentCategory){
        try{
            contentCategoryService.save(contentCategory);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/update")
    public Boolean update(@RequestBody ContentCategory contentCategory){
        try{
            contentCategoryService.update(contentCategory);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/delete")
    public Boolean delete(Long[] ids){
        try{
            contentCategoryService.deleteAll(ids);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @GetMapping("/findAll")
    public List<ContentCategory> findAll(){
        return contentCategoryService.findAll();
    }
}
