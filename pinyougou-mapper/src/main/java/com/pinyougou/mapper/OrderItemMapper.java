package com.pinyougou.mapper;

import com.pinyougou.pojo.OrderItem;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface OrderItemMapper extends Mapper<OrderItem> {
    List<Map<String,Object>> updateSaleCount();
}
