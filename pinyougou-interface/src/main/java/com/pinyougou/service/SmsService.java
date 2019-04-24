package com.pinyougou.service;

public interface SmsService {
    public Boolean sendSms(String phone,String signName,String templateCode,String templateParam);
}
