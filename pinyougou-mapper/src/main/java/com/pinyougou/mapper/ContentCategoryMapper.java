package com.pinyougou.mapper;

import com.pinyougou.pojo.ContentCategory;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ContentCategoryMapper extends Mapper<ContentCategory> {
    List<ContentCategory> findAll();
}
