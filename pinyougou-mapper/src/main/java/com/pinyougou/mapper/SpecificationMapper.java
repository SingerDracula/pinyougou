package com.pinyougou.mapper;

import com.pinyougou.pojo.Specification;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpecificationMapper extends Mapper<Specification> {
    List<Specification> findAll(Specification specification);


}
