package com.pinyougou.search.service.impl;


import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.mapper.OrderItemMapper;
import com.pinyougou.pojo.Item;
import com.pinyougou.solr.SolrItem;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TaskSaleCount {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrTemplate solrTamplate;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Scheduled(cron="0 15 10 * * ?")
    public void updateSaleCount(){
        List<Map<String, Object>> saleCount = orderItemMapper.updateSaleCount();
        ArrayList<SolrItem> solrItems = new ArrayList<>();
        for (Map map : saleCount) {
            SolrItem solrItem = new SolrItem();
            solrItem.setId(Long.valueOf(map.get("id").toString()));
            solrItem.setItem_salecount(Integer.parseInt(map.get("item_salecount").toString()));
            Item item = itemMapper.selectByPrimaryKey(Long.valueOf(map.get("id").toString()));
            solrItem.setBrand(item.getBrand());
            solrItem.setCategory(item.getCategory());
            solrItem.setGoodsId(item.getGoodsId());
            solrItem.setImage(item.getImage());
            solrItem.setPrice(item.getPrice());
            solrItem.setSeller(item.getSeller());
            solrItem.setTitle(item.getTitle());
            solrItem.setUpdateTime(item.getUpdateTime());
            Map specMap = JSON.parseObject(item.getSpec(), Map.class);
            solrItem.setSpecMap(specMap);



            solrItems.add(solrItem);
        }
        UpdateResponse updateResponse = solrTamplate.saveBeans(solrItems);
        if(updateResponse.getStatus() == 0){
            solrTamplate.commit();
        }else {
            solrTamplate.rollback();
        }
        System.out.println("结束");

    }

    public static void main(String[] args){
        ClassPathXmlApplicationContext Context = new ClassPathXmlApplicationContext("classpath" +
                ":applicationContext-service.xml");
        TaskSaleCount bean = Context.getBean(TaskSaleCount.class);
        bean.updateSaleCount();
    }
}
