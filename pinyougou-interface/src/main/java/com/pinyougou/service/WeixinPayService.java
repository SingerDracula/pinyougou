package com.pinyougou.service;

import java.util.Map;

public interface WeixinPayService {
    Map<String,String> getPayCode(String outTradeNo,String totalFee);

    Map<String, String> getPayStatus(String outTradeNo);

    Map<String, String> closeOrder(String outTradeNo);
}
