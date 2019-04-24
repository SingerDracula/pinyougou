package com.pinyougou.service;

import com.pinyougou.pojo.ItemCat;
import java.util.List;
import java.io.Serializable;
/**
 * ItemCatService 服务接口
 * @date 2019-03-27 16:06:13
 * @version 1.0
 */
public interface ItemCatService {

	/** 添加方法 */
	void save(ItemCat itemCat);

	/** 修改方法 */
	void update(ItemCat itemCat);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Long[] ids);

	/** 根据主键id查询 */
	ItemCat findOne(Long id);

	/** 查询全部 */
	List<ItemCat> findAll(Long parentId);

	/** 多条件分页查询 */
	List<ItemCat> findByPage(ItemCat itemCat, int page, int rows);

}