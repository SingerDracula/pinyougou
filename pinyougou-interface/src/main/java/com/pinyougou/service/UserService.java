package com.pinyougou.service;

import com.pinyougou.pojo.User;
import java.util.List;
import java.io.Serializable;

public interface UserService {

	/** 添加方法 */
	void save(User user);

	/** 修改方法 */
	void update(User user);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	User findOne(Serializable id);

	/** 查询全部 */
	List<User> findAll();

	/** 多条件分页查询 */
	List<User> findByPage(User user, int page, int rows);

	Boolean sendCode(String phone);

	boolean checkCode(String phone, String code);

    Boolean changePassword(String username, String password,String oldPassword);

	User showUser(String username);

	void changePhone(String phone, String remoteUser);
}