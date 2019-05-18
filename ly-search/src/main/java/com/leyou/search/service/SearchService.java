package com.leyou.search.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.utils.SearchRequest;

public interface SearchService {
    /**
     * 分页查询goods
     * @param searchRequest
     * @return
     */
    PageResult<Goods> queryGoodsByPage(SearchRequest searchRequest);
}
