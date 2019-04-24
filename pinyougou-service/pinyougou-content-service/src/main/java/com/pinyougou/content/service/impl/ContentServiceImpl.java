package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.ContentCategoryMapper;
import com.pinyougou.mapper.ContentMapper;
import com.pinyougou.pojo.Content;
import com.pinyougou.pojo.ContentCategory;
import com.pinyougou.service.ContentCategoryService;
import com.pinyougou.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Service(interfaceName = "com.pinyougou.service.ContentService")
@Transactional
public class ContentServiceImpl implements ContentService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ContentMapper contentMapper;

    @Override
    public void save(Content content) {

        contentMapper.insertSelective(content);
        redisTemplate.delete("content");
    }

    @Override
    public void update(Content content) {

        contentMapper.updateByPrimaryKeySelective(content);
        redisTemplate.delete("content");
    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {
        Example example = new Example(Content.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        contentMapper.deleteByExample(example);
        redisTemplate.delete("content");
    }

    @Override
    public Content findOne(Serializable id) {
        return null;
    }

    @Override
    public List<Content> findAll() {
        return null;
    }

    @Override
    public PageResult findByPage(int page, int rows) {
        PageInfo<Object> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                contentMapper.findAll();
            }
        });

        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public List<Content> findContentByCategoryId(int id) {
        List<Content> contentList =null;

        contentList = (List<Content>) redisTemplate.boundValueOps("content").get();
        if (contentList != null && contentList.size() > 0) {
            return contentList;
        }

        try{
                Example example = new Example(Content.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("categoryId",id);
                criteria.andEqualTo("status","1");
                example.orderBy("sortOrder").asc();
                contentList = contentMapper.selectByExample(example);
                try{
                    redisTemplate.boundValueOps("content").set(contentList);
                }catch (Exception ex){
                    return contentList;
                }

            }catch (Exception ex){
                throw new RuntimeException();
            }

        return contentList;

        }




}
