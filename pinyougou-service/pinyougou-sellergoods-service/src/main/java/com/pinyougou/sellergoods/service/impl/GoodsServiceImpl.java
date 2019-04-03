package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.Goods;
import com.pinyougou.pojo.Item;
import com.pinyougou.pojo.ItemCat;
import com.pinyougou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceName = "com.pinyougou.service.GoodsService")
@Transactional
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsDescMapper goodsDescMapper;
    @Autowired
    private SellerMapper sellerMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Override
    public void save(Goods goods) {
        goods.setAuditStatus("0");
        goodsMapper.insertSelective(goods);
        goods.getGoodsDesc().setGoodsId(goods.getId());
        goodsDescMapper.insertSelective(goods.getGoodsDesc());
        if("1".equalsIgnoreCase(goods.getIsEnableSpec())){
            for (Item item : goods.getItems()) {
                StringBuilder title = new StringBuilder();
                title.append(goods.getGoodsName());
                Map<String,Object> spec = JSON.parseObject(item.getSpec());
                for (Object value : spec.values()) {
                    title.append(""+value);
                }
                item.setTitle(title.toString());
                setItemInfo(item,goods);

                itemMapper.insertSelective(item);
            }
        }else {
            Item item = new Item();
            item.setTitle(goods.getGoodsName());
            item.setPrice(goods.getPrice());
            item.setNum(9999);
            item.setStatus("1");
            item.setIsDefault("1");
            item.setSpec("{}");
            setItemInfo(item,goods);
            itemMapper.insertSelective(item);
        }


    }

    public void setItemInfo(Item item,Goods goods){
        List<Map> maps = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
        if(maps != null && maps.size()>0){
            item.setImage((String)maps.get(0).get("url"));
        }
        item.setCategoryid(goods.getCategory3Id());
        item.setCreateTime(new Date());
        item.setUpdateTime(item.getCreateTime());
        item.setGoodsId(goods.getId());
        item.setSellerId(goods.getSellerId());
        item.setCategory(itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName());
        item.setBrand(brandMapper.selectByPrimaryKey(goods.getBrandId()).getName());
        item.setSeller(sellerMapper.selectByPrimaryKey(goods.getSellerId()).getNickName());
    }

    @Override
    public void update(Goods goods) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public Goods findOne(Serializable id) {
        return null;
    }

    @Override
    public List<Goods> findAll() {
        return null;
    }

    @Override
    public PageResult findByPage(Goods goods, int page, int rows) {
        PageInfo<Map<String,Object>> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                goodsMapper.findAll(goods);
            }
        });
        for (Map<String,Object> map : pageInfo.getList()) {
            ItemCat category1Id = itemCatMapper.selectByPrimaryKey(map.get("category1Id"));
            map.put("category1Name",category1Id != null?category1Id.getName():"");

            ItemCat category2Id = itemCatMapper.selectByPrimaryKey(map.get("category2Id"));
            map.put("category2Name",category2Id != null?category2Id.getName():"");

            ItemCat category3Id = itemCatMapper.selectByPrimaryKey(map.get("category3Id"));
            map.put("category3Name",category3Id != null?category3Id.getName():"");
        }
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }
}
