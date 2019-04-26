package com.pinyougou.user.service.Impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.UserMapper;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import com.pinyougou.user.service.utils.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.util.StringUtil;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service(interfaceName = "com.pinyougou.service.UserService")
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Value("${SmsUrl}")
    private String SmsUrl;
    @Value("${templateCode}")
    private String templateCode;
    @Value("${signName}")
    private String signName;

    @Override
    public void save(User user) {
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        userMapper.insertSelective(user);
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public User findOne(Serializable id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findByPage(User user, int page, int rows) {
        return null;
    }

    @Override
    public Boolean sendCode(String phone) {
        String code = UUID.randomUUID().toString().replaceAll("-","").replaceAll("[a-z|A-Z]","").substring(0,6);
        System.out.println(code);
        HttpClientUtils httpClientUtils = new HttpClientUtils(false);
        Map<String, String> map = new HashMap<>();
        map.put("phone",phone);
        map.put("signName",signName);
        map.put("templateCode",templateCode);
        map.put("templateParam","{'code':'"+code+"'}");
        String content = httpClientUtils.sendPost(SmsUrl, map);
        Map status = JSON.parseObject(content, Map.class);
        redisTemplate.boundValueOps(phone).set(code,90, TimeUnit.SECONDS);
        return (Boolean) status.get("status");

    }

    @Override
    public boolean checkCode(String phone, String code) {
        String userCode = redisTemplate.boundValueOps(phone).get();
        return StringUtils.isNoneBlank(userCode) && userCode.equals(code);
    }

    @Override
    public Boolean changePassword(String username, String password,String oldPassword) {
        User user1 = new User();
        user1.setUsername(username);

        User user = userMapper.selectOne(user1);
        System.out.println(user.getPassword());
        System.out.println(DigestUtils.md5DigestAsHex(oldPassword.getBytes()));
        if(DigestUtils.md5DigestAsHex(oldPassword.getBytes()).equals(user.getPassword()) ){
            user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
            userMapper.updateByPrimaryKey(user);
            return true;
        }else {
            return false;
        }
    }

    public User showUser(String username) {
        User user1 = new User();
        user1.setUsername(username);
        User user = userMapper.selectOne(user1);
        return user;
    }

    @Override
    public void changePhone(String phone, String remoteUser) {
        User user = new User();
        user.setUsername(remoteUser);
        User user1 = userMapper.selectOne(user);
        user1.setPhone(phone);
        userMapper.updateByPrimaryKey(user1);
    }

}
