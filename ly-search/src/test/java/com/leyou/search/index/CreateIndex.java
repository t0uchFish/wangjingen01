package com.leyou.search.index;

import com.leyou.search.LySearchService;
import com.leyou.search.pojo.Goods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchService.class)
public class CreateIndex {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Test
    public void createTemplate (){
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
    }
}
