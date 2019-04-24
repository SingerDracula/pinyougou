package com.pinyougou.search.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.Item;
import com.pinyougou.service.GoodsService;
import com.pinyougou.service.ItemSearchService;
import com.pinyougou.solr.SolrItem;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemMessageListener implements SessionAwareMessageListener<TextMessage> {
    @Reference(timeout = 10000)
    private GoodsService goodsService;
    @Reference(timeout = 10000)
    private ItemSearchService itemSearchService;
    @Override
    public void onMessage(TextMessage message, Session session) throws JMSException {
        String id = message.getText();
        System.out.println(id);
        List<Item> item = goodsService.findItemByGooDsId(Long.valueOf(id));
        System.out.println(item);
        ArrayList<SolrItem> solrItems = new ArrayList<>();
        for (Item item1 : item) {
            SolrItem solrItem = new SolrItem();
            solrItem.setId(item1.getId());
            solrItem.setBrand(item1.getBrand());
            solrItem.setCategory(item1.getCategory());
            solrItem.setGoodsId(item1.getGoodsId());
            solrItem.setImage(item1.getImage());
            solrItem.setPrice(item1.getPrice());
            solrItem.setSeller(item1.getSeller());
            solrItem.setTitle(item1.getTitle());
            solrItem.setUpdateTime(item1.getUpdateTime());
            Map specMap = JSON.parseObject(item1.getSpec(), Map.class);
            solrItem.setSpecMap(specMap);

            solrItems.add(solrItem);
        }
        itemSearchService.saveBeans(solrItems);
    }
}
