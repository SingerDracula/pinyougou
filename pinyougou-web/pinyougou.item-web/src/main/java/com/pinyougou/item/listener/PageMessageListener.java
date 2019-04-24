package com.pinyougou.item.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Item;
import com.pinyougou.service.GoodsService;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

public class PageMessageListener implements SessionAwareMessageListener<TextMessage> {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${page.dir}")
    private String pageDir;
    @Reference(timeout = 10000)
    private GoodsService goodsService;
    @Override
    public void onMessage(TextMessage textMessage, Session session) throws JMSException {

        try {
            String id = textMessage.getText();
            System.out.println(id);
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("item.ftl");
            Map<String, Object> goods = goodsService.getGoods(Long.valueOf(id));
            System.out.println(goods);
            OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(pageDir+id+".html"),"utf-8");
            template.process(goods,w);
            w.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
