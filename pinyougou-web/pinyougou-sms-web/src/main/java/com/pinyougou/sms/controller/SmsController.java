package com.pinyougou.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.service.SmsService;
import org.apache.solr.common.util.Hash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/sms")
public class SmsController {
    @Reference(timeout = 10000)
    private SmsService smsService;
    @PostMapping("/sendSms")
    public Map<String,Object> sendSms(String phone,String signName,String templateCode, String templateParam){
        Boolean status = smsService.sendSms(phone, signName, templateCode, templateParam);
        HashMap<String, Object> map = new HashMap<>();
        map.put("status",status);
        return map;
    }
}
