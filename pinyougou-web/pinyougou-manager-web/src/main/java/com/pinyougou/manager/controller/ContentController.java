package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Content;
import com.pinyougou.service.ContentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content")
public class ContentController {
    @Reference(timeout = 10000)
    private ContentService contentService;
    @RequestMapping("/findByPage")
    public PageResult findByPage(int page,int rows){
        return contentService.findByPage(page,rows);
    }

    @RequestMapping("/save")
    public Boolean save(@RequestBody Content content){
        try{
            contentService.save(content);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/update")
    public Boolean update(@RequestBody Content content){
        try{
            contentService.update(content);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/delete")
    public Boolean delete(Long[] ids){
        try{
            contentService.deleteAll(ids);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
