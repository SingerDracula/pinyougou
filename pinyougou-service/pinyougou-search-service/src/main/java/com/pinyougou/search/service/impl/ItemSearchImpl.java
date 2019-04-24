package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.Item;
import com.pinyougou.service.ItemSearchService;
import com.pinyougou.solr.SolrItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service(interfaceName = "com.pinyougou.service.ItemSearchService")
@Transactional
public class ItemSearchImpl implements ItemSearchService {
    @Autowired
    private SolrTemplate solrTemplate;
    @Override
    public Map<String, Object> search(Map<String, Object> params) {
        Map<String,Object> data = new HashMap<>();
        String keywords = (String) params.get("keywords");
        if(StringUtils.isNoneBlank(keywords)){
            HighlightQuery highlightQuery = new SimpleHighlightQuery();

            Integer page = (Integer) params.get("page");
            if(page == null){
                page=1;
            }
            Integer rows = (Integer) params.get("rows");
            if(rows == null){
                rows=20;
            }

            highlightQuery.setOffset((page-1)*rows);
            highlightQuery.setRows(rows);

            if(!"".equals(params.get("category"))){
                Criteria criteria = new Criteria("category").is(params.get("category"));

                highlightQuery.addCriteria(criteria);
            }
            if(!"".equals(params.get("brand"))){
                Criteria criteria = new Criteria("brand").is(params.get("brand"));

                highlightQuery.addCriteria(criteria);
            }
            if(params.get("spec")!=null){
                Map<String,String> spec = (Map<String, String>) params.get("spec");
                Set<String> keys = spec.keySet();
                for (String key : keys) {

                    Criteria criteria = new Criteria("spec_"+key).is(spec.get(key));
                    highlightQuery.addCriteria(criteria);
                }

            }
            if(!"".equals(params.get("price"))){
                String[] split = params.get("price").toString().split("-");
                if(!split[0].equals("0")){
                    Criteria criteria = new Criteria("price").greaterThanEqual(split[0]);
                    highlightQuery.addCriteria(criteria);
                }
                if(!split[1].equals("*")){
                    Criteria criteria = new Criteria("price").lessThanEqual(split[1]);
                    highlightQuery.addCriteria(criteria);
                }
            }

            String sortValue = (String) params.get("sort");
            String sortField = (String) params.get("sortField");
            if(StringUtils.isNoneBlank(sortValue) && StringUtils.isNoneBlank(sortField)){
                Sort sort = new Sort("ASC".equalsIgnoreCase(sortValue)?Sort.Direction.ASC:Sort.Direction.DESC,sortField);
                highlightQuery.addSort(sort);
            }


            HighlightOptions highlightOptions = new HighlightOptions();
            highlightOptions.addField("title");
            highlightOptions.setSimplePrefix("<font color='red'>");
            highlightOptions.setSimplePostfix("</font>");
            highlightQuery.setHighlightOptions(highlightOptions);

            Criteria criteria = new Criteria("keywords").is(keywords);
            highlightQuery.addCriteria(criteria);
            HighlightPage<SolrItem> highlightPage = solrTemplate.queryForHighlightPage(highlightQuery, SolrItem.class);
            for (HighlightEntry<SolrItem> he : highlightPage.getHighlighted()) {
                SolrItem solrItem = he.getEntity();
                if(he.getHighlights().size()>0 && he.getHighlights().get(0).getSnipplets().size()>0){
                    solrItem.setTitle(he.getHighlights().get(0).getSnipplets().get(0));
                }
            }
            data.put("rows",highlightPage.getContent());
            data.put("totalPage",highlightPage.getTotalPages());
            data.put("total",highlightPage.getTotalElements());
        }else{
            Query query = new SimpleQuery("*:*");
            Integer page = (Integer) params.get("page");
            if(page == null){
                page=1;
            }
            Integer rows = (Integer) params.get("rows");
            if(rows == null){
                rows=20;
            }
            query.setOffset((page-1)*rows);
            query.setRows(rows);

            ScoredPage<SolrItem> solrItems = solrTemplate.queryForPage(query, SolrItem.class);
            data.put("rows",solrItems.getContent());
            data.put("totalPage",solrItems.getTotalPages());
            data.put("total",solrItems.getTotalElements());
        }
        System.out.println(data);
        return data;

    }

    @Override
    public void saveBeans(ArrayList<SolrItem> solrItems) {
        UpdateResponse updateResponse = solrTemplate.saveBeans(solrItems);
        if (updateResponse.getStatus() == 0){
            solrTemplate.commit();
        }else{
            solrTemplate.rollback();;
        }

    }

    @Override
    public void delete(List<Long> goodsId) {
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("goodsId").in(goodsId);
        query.addCriteria(criteria);
        UpdateResponse updateResponse = solrTemplate.delete(query);
        if (updateResponse.getStatus() == 0){
            solrTemplate.commit();
        }else{
            solrTemplate.rollback();
        }
    }
}
