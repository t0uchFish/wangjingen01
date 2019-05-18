package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据分类id查询规格分组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroups(@PathVariable("cid") long cid) {
        List<SpecGroup> specGroups = specificationService.querySpecGroupsById(cid);
        if (specGroups != null && specGroups.size() > 0) {
            return ResponseEntity.ok(specGroups);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 根据分组id查询规格参数
     *
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParam(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching,
            @RequestParam(value = "generic", required = false) Boolean generic
    ) {

        List<SpecParam> specParams = specificationService.querySpecParamByGid(gid, cid, searching,generic);
        if (specParams != null && specParams.size() > 0) {
            return ResponseEntity.ok(specParams);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //    /item/spec/group'

    /**
     * 保存规格分组
     * @param specGroup
     * @return
     */
    @PostMapping("group")
    public ResponseEntity<Void> saveSpecGroup(@RequestBody SpecGroup specGroup) {
        specificationService.saveSpecGroup(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //    url: '/item/spec/group',

    /**
     *修改规格分组
     * @param specGroup
     * @return
     */
    @PutMapping("group")
    public ResponseEntity<Void> updateSpecGroup(@RequestBody SpecGroup specGroup) {
        specificationService.updateSpecGroup(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //     this.$http.delete("/item/spec/group/" + id)

    /**
     * 删除规格分组
     * @param id
     * @return
     */
    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> deleteSpecGroup(@PathVariable("id") long id) {
        specificationService.deleteSpecGroupById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 保存规格参数
     * @param specParam
     * @return
     */
    @PostMapping("param")
    public ResponseEntity<Void> saveSpecParam(@RequestBody SpecParam specParam){
        specificationService.saveSpecParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //    method: this.isEdit ? 'put' : 'post',
//    url: '/item/spec/param',

    /**
     * 修改规格参数
     * @param specParam
     * @return
     */
    @PutMapping("param")
    public ResponseEntity<Void> updateSpecParam(@RequestBody SpecParam specParam) {
        specificationService.updateSpecParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //       this.$http.delete("/item/spec/param/" + id)

    /**
     * 根据规格参数id删除参数
     * @param id
     * @return
     */
    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> deleteSpecParam(@PathVariable("id") Long id) {
        specificationService.deleteSpecParamById(id);
        return ResponseEntity.ok().build();
    }





}
