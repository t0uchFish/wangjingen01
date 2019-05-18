package com.leyou.search;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.SpuBo;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.imp.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchService.class)
public class ESLoadDataTest {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private IndexService indexService;
    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void loadData(){
        int page = 1;
        int rows = 10;
        ArrayList<Goods> goodsList = new ArrayList<>();
        while (true) {
            PageResult<SpuBo> result = goodsClient.querySpuByPage(page, rows,null,true);

            if (null == result) {
                break;
            }
            List<SpuBo> SpuBos = result.getItems();
            SpuBos.forEach( spuBo -> {
                Goods goods = indexService.buildGoods(spuBo);
                goodsList.add(goods);
            });
            page++;
        }
        goodsRepository.saveAll(goodsList);

    }
}
