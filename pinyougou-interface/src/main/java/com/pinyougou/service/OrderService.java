package com.pinyougou.service;

import com.pinyougou.pojo.Order;
import com.pinyougou.pojo.PayLog;

import java.util.List;
import java.io.Serializable;
import java.util.Map;

/**
 * OrderService 服务接口
 * @date 2019-03-27 16:06:13
 * @version 1.0
 */
public interface OrderService {

	/** 添加方法 */
	void save(Order order);

	/** 修改方法 */
	void update(Order order);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	Order findOne(Serializable id);

	/** 查询全部 */
	List<Order> findAll();

	/** 多条件分页查询 */
	List<Order> findByPage(Order order, int page, int rows);

    PayLog getPayLogByRedis(String user);

	void updateStatus(String outTradeNo, String transaction_id);

	List<Map<String, Object>> findMyOrder(String username);
}