package com.leyou.search.client;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.SpuBo;
import com.leyou.search.LySearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchService.class)
public class GoodsClientTest {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired


    @Test
    public void testQueryCategories() {
        PageResult<SpuBo> result = goodsClient.querySpuByPage(1, 10,null,null);
        result.getItems().forEach(item -> {
            System.out.println(item.getId());
        });
    }

    @Test
    public void testQueryNamesByCid() {
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(76L);
        List<Category> categories = categoryClient.queryCategoryByIds(longs);
        System.out.println(categories);
    }


}