package com.leyou.search.controller;


import com.leyou.common.pojo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.SearchService;
import com.leyou.search.utils.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class SearchController {
    @Autowired
    private SearchService searchService;

    /**
     * 分页查询goods
     * @param searchRequest
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> queryGoodsByPage(@RequestBody SearchRequest searchRequest) {
        PageResult<Goods> goodsList = searchService.queryGoodsByPage(searchRequest);
        if (goodsList != null && goodsList.getItems() != null && goodsList.getItems().size() > 0) {
            return ResponseEntity.ok(goodsList);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
