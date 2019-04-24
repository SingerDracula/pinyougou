package com.pinyougou.mapper;

import com.pinyougou.pojo.Content;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ContentMapper extends Mapper<Content> {
    List<Content> findAll();
}
