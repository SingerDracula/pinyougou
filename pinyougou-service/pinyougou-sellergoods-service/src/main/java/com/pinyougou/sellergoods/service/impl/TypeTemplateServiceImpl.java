package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.mapper.TypeTemplateMapper;
import com.pinyougou.pojo.SpecificationOption;
import com.pinyougou.pojo.TypeTemplate;
import com.pinyougou.service.TypeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service(interfaceName = "com.pinyougou.service.TypeTemplateService")
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {
    @Autowired
    private TypeTemplateMapper typeTemplateMapper;
    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;

    @Override
    public void save(TypeTemplate typeTemplate) {
        typeTemplateMapper.insertSelective(typeTemplate);
    }

    @Override
    public void update(TypeTemplate typeTemplate) {
        typeTemplateMapper.updateByPrimaryKeySelective(typeTemplate);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void deleteAll(Long[] ids) {
        Example example = new Example(TypeTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        typeTemplateMapper.deleteByExample(example);
    }

    @Override
    public Map<String,Object> findOne(Long id) {

        return typeTemplateMapper.findOneByIdItemCat(id);
    }

    @Override
    public List<TypeTemplate> findAll() {
        return null;
    }

    @Override
    public List<Map<String,Object>> findUserByItemCat() {
        return typeTemplateMapper.findUserByItemCat();
    }

    @Override
    public PageResult findByPage(TypeTemplate typeTemplate, int page, int rows) {
        PageInfo<TypeTemplate> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                typeTemplateMapper.findAll(typeTemplate);
            }
        });
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public List<Map<String, Object>> findAllBrand() {
        return typeTemplateMapper.findAllBrand();

    }

    @Override
    public List<Map<String, Object>> findSpecList() {
        return typeTemplateMapper.findSpecList();
    }

    public TypeTemplate findBrandIdsByTemplateId(Long id){
        return typeTemplateMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Map> findSpecByTemplateId(Long id) {
        TypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
        List<Map> specList = JSON.parseArray(typeTemplate.getSpecIds(), Map.class);

        for (Map spec : specList) {
            SpecificationOption specificationOption = new SpecificationOption();
            specificationOption.setSpecId(Long.valueOf(spec.get("id").toString()));
            List<SpecificationOption> options = specificationOptionMapper.select(specificationOption);
            spec.put("options",options);
        }

        return specList;
    }
}
