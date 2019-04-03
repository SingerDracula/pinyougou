package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.ItemCatMapper;
import com.pinyougou.pojo.ItemCat;
import com.pinyougou.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
@Service(interfaceName = "com.pinyougou.service.ItemCatService")
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private ItemCatMapper itemCatMapper;

    @Override
    public void save(ItemCat itemCat) {
        try{
            itemCatMapper.insertSelective(itemCat);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    @Override
    public void update(ItemCat itemCat) {
        itemCatMapper.updateByPrimaryKeySelective(itemCat);
    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Long[] ids) {
        Example example = new Example(ItemCat.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        itemCatMapper.deleteByExample(example);
    }

    @Override
    public ItemCat findOne(Long id) {
        return null;
    }

    @Override
    public List<ItemCat> findAll(Long parentId) {
        return itemCatMapper.findAllByParentId(parentId);
    }

    @Override
    public List<ItemCat> findByPage(ItemCat itemCat, int page, int rows) {
        return null;
    }
}
