package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {

    /**
     * 用户登录功能
     * @param userName
     * @param password
     * @return
     */
    @PostMapping("user/query")
    User queryUser(
            @RequestParam("username") String userName,
            @RequestParam("password") String password);
}
