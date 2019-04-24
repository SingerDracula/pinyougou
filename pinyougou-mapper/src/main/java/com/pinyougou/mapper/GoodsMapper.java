package com.pinyougou.mapper;

import com.pinyougou.pojo.Goods;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface GoodsMapper extends Mapper<Goods> {
    List<Map<String,Object>> findAll(Goods goods);

    void updateStatus(@Param("ids") Long[] ids,@Param("status") String status);

    void updateDeleteStatus(@Param("ids")Serializable[] ids);

    void updateMarket(@Param("ids") Long[] ids,@Param("status") String status);
}
