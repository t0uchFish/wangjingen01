package com.leyou.item.api;


import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationApi {

    /**
     * 根据分组id查询规格参数
     * @param gid
     * @return
     */
    @GetMapping("spec/params")
    List<SpecParam> querySpecParam(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching,
            @RequestParam(value = "generic", required = false) Boolean generic
    );

    /**
     * 根据分类id查询规格分组
     * @param cid
     * @return
     */
    @GetMapping("spec/groups/{cid}")
    List<SpecGroup> querySpecGroups(@PathVariable("cid") long cid);


}
