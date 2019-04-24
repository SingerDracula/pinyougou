package com.pinyougou.service;

import com.pinyougou.solr.SolrItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ItemSearchService {
    Map<String,Object> search(Map<String,Object> params);

    void saveBeans(ArrayList<SolrItem> solrItems);

    void delete(List<Long> asList);
}
