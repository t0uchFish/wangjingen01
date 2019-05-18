package com.leyou.auth.service;

import com.leyou.auth.entiy.UserInfo;
import com.leyou.user.pojo.User;

public interface AuthService {
    String authentication(String username, String password);


}
