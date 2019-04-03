package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.SpecificationMapper;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.SpecificationOption;
import com.pinyougou.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
@Service(interfaceName = "com.pinyougou.service.SpecificationService")
@Transactional
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private SpecificationMapper specificationMapper;
    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;


    @Override
    public void save(Specification specification) {
        try {
            specificationMapper.insertSelective(specification);
            System.out.println(specification.getId());
            specificationOptionMapper.save(specification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Specification specification) {
        specificationMapper.updateByPrimaryKeySelective(specification);
        SpecificationOption specificationOption = new SpecificationOption();
        specificationOption.setSpecId(specification.getId());
        specificationOptionMapper.delete(specificationOption);
        specificationOptionMapper.save(specification);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void deleteAll(Long[] ids) {
        Example example = new Example(Specification.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        specificationMapper.deleteByExample(example);

        for (Long id : ids) {
            SpecificationOption specificationOption = new SpecificationOption();
            specificationOption.setSpecId(id);
            specificationOptionMapper.delete(specificationOption);
        }
    }

    @Override
    public Specification findOne(Serializable id) {
        return null;
    }

    @Override
    public List<Specification> findAll() {
        return null;
    }

    @Override
    public PageResult findByPage(Specification specification, int page, int rows) {
        PageInfo<Specification> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                specificationMapper.findAll(specification);
            }
        });
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public List<SpecificationOption> findOptions(Long id) {
        SpecificationOption specificationOption = new SpecificationOption();
        specificationOption.setSpecId(id);
        return specificationOptionMapper.select(specificationOption);

    }

}
