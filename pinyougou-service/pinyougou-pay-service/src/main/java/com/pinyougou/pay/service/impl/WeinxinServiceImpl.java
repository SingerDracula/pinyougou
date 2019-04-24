package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.common.utils.HttpClientUtils;

import com.pinyougou.service.WeixinPayService;


import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
@Service(interfaceName = "com.pinyougou.service.WeixinPayService")
public class WeinxinServiceImpl implements WeixinPayService {
    @Value("${appid}")
    private String appid;
    @Value("${partner}")
    private String partner;
    @Value("${partnerkey}")
    private String partnerkey;
    @Value("${unifiedorder}")
    private String unifiedorder;
    @Value("${orderquery}")
    private String orderquery;
    @Value("${closeorder}")
    private String closeorder;


    @Override
    public Map<String, String> getPayCode(String outTradeNo, String totalFee) {
        Map<String,String> params = new HashMap<>();
        params.put("appid",appid);
        params.put("mch_id",partner);
        params.put("nonce_str", WXPayUtil.generateNonceStr());
        params.put("body", "品优购");
        params.put("out_trade_no", outTradeNo);
        params.put("total_fee", totalFee);
        params.put("spbill_create_ip","127.0.0.1");
        params.put("notify_url","http://test.itcast.cn");
        params.put("trade_type","NATIVE");
        try {
            String xml = WXPayUtil.generateSignedXml(params, partnerkey);
            HttpClientUtils httpClientUtils = new HttpClientUtils(true);
            String result = httpClientUtils.sendPost(unifiedorder, xml);
            Map<String, String> map = WXPayUtil.xmlToMap(result);
            HashMap<String, String> resultMap = new HashMap<>();
            resultMap.put("codeUrl",map.get("code_url"));
            resultMap.put("totalFee",totalFee);
            resultMap.put("outTradeNo",outTradeNo);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> getPayStatus(String outTradeNo) {
        Map<String, String> params = new HashMap<>();

        params.put("appid", appid);
        params.put("mch_id", partner);
        params.put("nonce_str", WXPayUtil.generateNonceStr());
        params.put("out_trade_no", outTradeNo);


        try {
            String xml = WXPayUtil.generateSignedXml(params, partnerkey);
            HttpClientUtils httpClientUtils = new HttpClientUtils(true);
            String result = httpClientUtils.sendPost(orderquery, xml);
            Map<String, String> map = WXPayUtil.xmlToMap(result);



            return map;

        } catch (Exception e) {
            e.printStackTrace();

        }
       return null;
    }

    @Override
    public Map<String, String> closeOrder(String outTradeNo) {
        Map<String, String> params = new HashMap<>();

        params.put("appid", appid);
        params.put("mch_id", partner);
        params.put("nonce_str", WXPayUtil.generateNonceStr());
        params.put("out_trade_no", outTradeNo);


        try {
            String xml = WXPayUtil.generateSignedXml(params, partnerkey);
            HttpClientUtils httpClientUtils = new HttpClientUtils(true);
            String result = httpClientUtils.sendPost(closeorder, xml);
            Map<String, String> map = WXPayUtil.xmlToMap(result);



            return map;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
}
