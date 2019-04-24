package com.pinyougou.item.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.File;
import java.io.Serializable;

public class PageDeleteMessageListener implements SessionAwareMessageListener<ObjectMessage> {
    @Value("${page.dir}")
    private String pageDir;
    @Override
    public void onMessage(ObjectMessage objectMessage, Session session) throws JMSException {
        Long[] ids = (Long[]) objectMessage.getObject();
        for (Long id : ids) {
            File file = new File(pageDir+id+".html");
            if(file.exists()){
                file.delete();
            }
        }

    }
}
