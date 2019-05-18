package com.leyou;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.LyUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyUserService.class)
public class numberUtils {
//    @Autowired
//    private NumberUtils numberUtils;
    @Test
    public void testNumber() {
        String s = NumberUtils.generateCode(6);
        System.out.println(s);

    }
}
