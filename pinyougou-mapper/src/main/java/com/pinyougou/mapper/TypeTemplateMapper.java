package com.pinyougou.mapper;

import com.pinyougou.pojo.TypeTemplate;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;
import java.util.function.ObjIntConsumer;

public interface TypeTemplateMapper extends Mapper<TypeTemplate> {

    List<TypeTemplate> findAll(TypeTemplate typeTemplate);

    @Select("select id,name as text from tb_brand order by id")
    List<Map<String,Object>> findAllBrand();

    @Select("select id,spec_name as text from tb_specification order by id")
    List<Map<String,Object>> findSpecList();

    @Select("select id,name as text from tb_type_template order by id")
    List<Map<String,Object>> findUserByItemCat();

    @Select("select id,name as text from tb_type_template where id = #{id}")
    Map<String,Object> findOneByIdItemCat(Long id);
}
