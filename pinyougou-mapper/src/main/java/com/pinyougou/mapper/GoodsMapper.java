package com.pinyougou.mapper;

import com.pinyougou.pojo.Goods;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface GoodsMapper extends Mapper<Goods> {
    List<Map<String,Object>> findAll(Goods goods);
}
