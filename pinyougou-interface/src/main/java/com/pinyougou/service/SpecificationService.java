package com.pinyougou.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.SpecificationOption;

import java.util.List;
import java.io.Serializable;
/**
 * SpecificationService 服务接口
 * @date 2019-03-27 16:06:13
 * @version 1.0
 */
public interface SpecificationService {

	/** 添加方法 */
	void save(Specification specification);

	/** 修改方法 */
	void update(Specification specification);

	/** 根据主键id删除 */
	void delete(Long id);

	/** 批量删除 */
	void deleteAll(Long[] ids);

	/** 根据主键id查询 */
	Specification findOne(Serializable id);

	/** 查询全部 */
	List<Specification> findAll();

	/** 多条件分页查询 */
	PageResult findByPage(Specification specification, int page, int rows);

	List<SpecificationOption> findOptions(Long id);

}