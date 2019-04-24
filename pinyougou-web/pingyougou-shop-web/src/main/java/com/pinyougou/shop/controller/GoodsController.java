package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.support.Parameter;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Goods;
import com.pinyougou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Reference(timeout = 10000)
    private GoodsService goodsService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination queue;
    @Autowired
    private Destination deleteQueue;
    @Autowired
    private Destination pageTopic;
    @Autowired
    private Destination pageDeleteTopic;

    @PostMapping("/save")
    public Boolean save(@RequestBody Goods goods){
        try {
            goods.setSellerId(SecurityContextHolder.getContext().getAuthentication().getName());
            goodsService.save(goods);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/findByPage")
    public PageResult findByPage(int page,int rows,Goods goods){
        String sellerName = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(sellerName);
        if(goods != null && StringUtil.isNotEmpty(goods.getGoodsName())){
            try {
                goods.setGoodsName(new String(goods.getGoodsName().getBytes("ISO8859-1"),"utf-8"));
            } catch (UnsupportedEncodingException e) {


            }
        }
        System.out.println(goodsService.findByPage(goods,page,rows));
        return goodsService.findByPage(goods,page,rows);
    }

    @GetMapping("/updateStatus")
    public Boolean updateStatus(Long[] ids, String status){
        try{
            goodsService.updateStatus(ids,status);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/delete")
    public Boolean delete(Long[] ids){
        try{
            goodsService.deleteAll(ids);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/updateMarket")
    public Boolean updateMarket(Long[] ids, String status){
        try{
            goodsService.updateMarket(ids,status);
            if("1".equals(status)){
                jmsTemplate.send(queue, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createObjectMessage(ids);
                    }
                });

                for (Long id : ids) {
                    jmsTemplate.send(pageTopic, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            return session.createTextMessage(id.toString());
                        }
                    });
                }

            }else{
                jmsTemplate.send(deleteQueue,new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createObjectMessage(ids);
                    }
                });


                    jmsTemplate.send(pageDeleteTopic, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            return session.createObjectMessage(ids);
                        }
                    });

            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
