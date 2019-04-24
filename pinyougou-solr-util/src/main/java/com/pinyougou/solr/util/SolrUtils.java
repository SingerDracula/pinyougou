package com.pinyougou.solr.util;


import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.GoodsMapper;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.pojo.Item;
import com.pinyougou.solr.SolrItem;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SolrUtils {
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTamplate;

    public void imporItemtData(){
        Item item = new Item();
        item.setStatus("1");
        List<Item> itemList = itemMapper.select(item);
        ArrayList<SolrItem> solrItems = new ArrayList<>();
        for (Item item1 : itemList) {
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
                ":applicationContext.xml");
        SolrUtils bean = Context.getBean(SolrUtils.class);
        bean.imporItemtData();
    }
}
