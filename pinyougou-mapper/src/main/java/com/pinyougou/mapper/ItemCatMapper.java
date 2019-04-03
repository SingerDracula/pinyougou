package com.pinyougou.mapper;

import com.pinyougou.pojo.ItemCat;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ItemCatMapper extends Mapper<ItemCat> {
    @Select("select * from tb_item_cat where parent_id = #{parentId}")
    List<ItemCat> findAllByParentId(Long parentId);

}
