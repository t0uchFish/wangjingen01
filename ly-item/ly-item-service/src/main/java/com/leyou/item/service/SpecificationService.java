package com.leyou.item.service;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;

import java.util.List;

public interface SpecificationService {
    /**
     * 根据分类id查询规格分组
     * @param cid
     * @return
     */
    List<SpecGroup> querySpecGroupsById(long cid);

    /**
     * 根据分组id查询规格参数
     * @param gid
     * @return
     */
    List<SpecParam> querySpecParamByGid(Long gid, Long cid,Boolean searching,Boolean generic);

    /**
     * 保存规格分组
     * @param specGroup
     */
    void saveSpecGroup(SpecGroup specGroup);

    /**
     * 修改规格分组
     * @param specGroup
     */
    void updateSpecGroup(SpecGroup specGroup);

    /**
     * 删除规格分组
     * @param id
     */
    void deleteSpecGroupById(long id);

    /**
     * 保存规格参数
     * @param specParam
     */
    void saveSpecParam(SpecParam specParam);

    /**
     * 修改规格参数
     */
    void updateSpecParam( SpecParam specParam);

    /**
     * 根据规格参数id删除参数
     * @param id
     */
    void deleteSpecParamById(Long id);
}

