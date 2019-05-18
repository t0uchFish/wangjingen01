package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;


    @Override
    public List<SpecGroup> querySpecGroupsById(long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> groups = specGroupMapper.select(specGroup);
        SpecParam specParam = new SpecParam();
        groups.forEach(group ->{
            specParam.setGroupId(group.getId());
            group.setSpecParams(specParamMapper.select(specParam));
        });
        return groups;
    }

    @Override
    public List<SpecParam> querySpecParamByGid(Long gid, Long cid,Boolean searching,Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);
        return specParamMapper.select(specParam);
    }

    @Override
    public void saveSpecGroup(SpecGroup specGroup) {
        specGroupMapper.insert(specGroup);
    }

    @Override
    public void updateSpecGroup(SpecGroup specGroup) {
        specGroupMapper.updateByPrimaryKeySelective(specGroup);
    }

    @Override
    @Transactional
    public void deleteSpecGroupById(long id) {
        SpecGroup specGroup = new SpecGroup();
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(id);
        specGroup.setId(id);
        this.specGroupMapper.deleteByPrimaryKey(specGroup);
        this.specParamMapper.delete(specParam);
    }

    @Override
    public void saveSpecParam(SpecParam specParam) {
        specParamMapper.insert(specParam);
    }

    @Override
    public void updateSpecParam(SpecParam SpecParam) {
        specParamMapper.updateByPrimaryKeySelective(SpecParam);
    }

    @Override
    public void deleteSpecParamById(Long id) {
        SpecParam specParam = new SpecParam();
        specParam.setId(id);
        specParamMapper.deleteByPrimaryKey(specParam);
    }




}
