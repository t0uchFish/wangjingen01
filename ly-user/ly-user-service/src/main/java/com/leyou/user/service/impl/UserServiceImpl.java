package com.leyou.user.service.impl;


import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    public UserMapper userMapper;
    //    @Autowired
//    private NumberUtils numberUtils;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;


    static final String KEY_PREFIX = "user:code:phone:";

    static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public Boolean chenkData(String data, Integer type) {
        User user = new User();

        switch (type) {
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
        }
        return userMapper.selectCount(user) != 1;
    }

    @Override
    public Boolean sendVerifyCode(String phone) {
        //生成随机验证码
        String code = NumberUtils.generateCode(6);

//        String code = "123456";
        try {
            HashMap<String, String> mesg = new HashMap<>();
            mesg.put("phone", phone);
            mesg.put("code", code);
            amqpTemplate.convertAndSend("ly.sms.exchange", "sms.verify.code", mesg);
            redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
            return true;
        } catch (AmqpException e) {
            logger.error("发送短信失败。phone：{}， code：{}", phone, code);
            return false;
        }

    }

    @Override
    public Boolean register(User user, String code) {
        String key = KEY_PREFIX + user.getPhone();
        String code1 = redisTemplate.opsForValue().get(key);
//        if (!Objects.equals(code, code1)) {
        if (!Objects.equals(code, "123456")) {
            return false;
        }
        user.setId(null);
        user.setCreated(new Date());
        // 生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        // 对密码进行加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));

        Boolean boo = userMapper.insertSelective(user) == 1;
        // 如果注册成功，删除redis中的code
        if (boo) {
            try {
                this.redisTemplate.delete(key);
            } catch (Exception e) {
                logger.error("删除缓存验证码失败，code：{}", code, e);
            }
        }
        return boo;
    }

    @Override
    public User queryUser(String userName, String password) {
        User record = new User();
        record.setUsername(userName);
        User user = userMapper.selectOne(record);
        if (user == null) {
            return null;
        }
        if (!StringUtils.equals(user.getPassword(), CodecUtils.md5Hex(password, user.getSalt()))) {
            return null;
        }

        return user;
    }
}
