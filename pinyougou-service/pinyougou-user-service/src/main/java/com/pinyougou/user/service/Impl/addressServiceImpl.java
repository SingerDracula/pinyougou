package com.pinyougou.user.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.AddressMapper;
import com.pinyougou.pojo.Address;
import com.pinyougou.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.List;
@Service(interfaceName = "com.pinyougou.service.AddressService")
public class addressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;
    @Override
    public void save(Address address) {

    }

    @Override
    public void update(Address address) {
        if(address.getNotes().equals("1")){
            address.setNotes("家");
        }
        if(address.getNotes().equals("2")){
            address.setNotes("父母家");
        }
        if(address.getNotes().equals("3")){
            address.setNotes("公司");
        }
        addressMapper.updateByPrimaryKeySelective(address);
    }

    @Override
    public void delete(Serializable id) {
        addressMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public Address findOne(Serializable id) {
        return null;
    }

    @Override
    public List<Address> findAll(String userId) {
        return null;
    }

    @Override
    public List<Address> findByPage(Address address, int page, int rows) {
        return null;
    }

    @Override
    public List<Address> findAddress(String sellerId) {
        Address address = new Address();
        address.setUserId(sellerId);
        return addressMapper.select(address);
    }

    @Override
    public void delmodityete(Long id) {
        Address address = new Address();
        address.setIsDefault("1");
        Address address1 = addressMapper.selectOne(address);
        address1.setIsDefault("0");
        addressMapper.updateByPrimaryKeySelective(address1);

        address.setId(id);
        addressMapper.updateByPrimaryKeySelective(address);
    }

}
