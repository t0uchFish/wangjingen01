package com.leyou.item.mapper;

import com.leyou.item.pojo.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SkuMapper extends Mapper<Sku> {
    @Select("SELECT id FROM tb_sku where spu_id = #{id}")
    List<Long> querySkuIdBySpuId(@Param("id") Long id);

//    /改：update 表名 set 列名=值 ，列名=值 where 条件 ；
    @Update("update tb_sku set last_update_time = NOW(),enable=0 where id = #{id}")
    void updateSkuByEnableById(@Param("id") Long skuId);
}
