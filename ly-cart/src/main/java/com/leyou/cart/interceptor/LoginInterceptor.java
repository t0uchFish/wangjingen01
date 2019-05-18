package com.leyou.cart.interceptor;

import com.leyou.auth.entiy.JwtUtils;
import com.leyou.auth.entiy.UserInfo;
import com.leyou.cart.config.JwtProperties;
import com.leyou.common.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@EnableConfigurationProperties(JwtProperties.class)
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtProperties jwtProps;

    // 定义一个线程域，存放登录用户
    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();
    public LoginInterceptor(JwtProperties jwtProperties) {
        this.jwtProps = jwtProperties;
    }
    //前置方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取cookie
        String token = CookieUtils.getCookieValue(request, jwtProps.getCookieName());
        UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProps.getPublicKey());
        if (userInfo == null) {
            return false;
        }
        tl.set(userInfo);

        return true;
    }

    //后置方法
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
    //后置方法
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        tl.remove();
    }
    public static UserInfo getUserInfo(){
        return tl.get();
    }


}
