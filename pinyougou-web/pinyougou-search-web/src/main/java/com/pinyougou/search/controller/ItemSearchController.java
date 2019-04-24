package com.pinyougou.search.controller;


import com.alibaba.dubbo.config.annotation.Reference;

import com.pinyougou.service.ItemSearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/itemSearch")
public class ItemSearchController {
    @Reference(timeout = 30000)
    private ItemSearchService itemSearchService;
    @RequestMapping("/search")
    public Map<String,Object> search(@RequestBody Map<String,Object> searchParam){
        return itemSearchService.search(searchParam);
    }

}
