package com.pinyougou.search.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.service.ItemSearchService;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;
import java.util.Arrays;

public class ItemDeleteListener implements SessionAwareMessageListener<ObjectMessage> {
    @Reference(timeout = 10000)
    private ItemSearchService itemSearchService;
    @Override
    public void onMessage(ObjectMessage o, Session session) throws JMSException {
        Long[] ids  = (Long[]) o.getObject();
        itemSearchService.delete(Arrays.asList(ids));
    }
}
