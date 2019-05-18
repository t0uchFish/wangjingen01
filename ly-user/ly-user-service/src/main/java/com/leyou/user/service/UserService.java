package com.leyou.user.service;

import com.leyou.user.pojo.User;

public interface UserService {
    /**
     * 校验注册数据
     * @param data
     * @param type
     * @return
     */
    Boolean chenkData(String data, Integer type);

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    Boolean sendVerifyCode(String phone);

    /**
     * 用户注册
     * @param user
     * @return
     */
    Boolean register(User user,String code);
    /**
     * 用户登录功能
     * @param userName
     * @param password
     * @return
     */
    User queryUser(String userName, String password);
}
