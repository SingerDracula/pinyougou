package com.pinyougou.service;

import com.pinyougou.pojo.Address;
import java.util.List;
import java.io.Serializable;
/**
 * AddressService 服务接口
 * @date 2019-03-27 16:06:13
 * @version 1.0
 */
public interface AddressService {

	/** 添加方法 */
	void save(Address address);

	/** 修改方法 */
	void update(Address address);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	Address findOne(Serializable id);

	/** 查询全部 */
	List<Address> findAll(String userId);

	/** 多条件分页查询 */
	List<Address> findByPage(Address address, int page, int rows);

    List<Address> findAddress();
}