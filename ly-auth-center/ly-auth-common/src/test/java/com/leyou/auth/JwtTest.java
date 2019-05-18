package com.leyou.auth;

import com.leyou.auth.entiy.JwtUtils;
import com.leyou.auth.entiy.UserInfo;
import com.leyou.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {
    private static final String pubKeyPath = "F:/heima/rsa/rsa.pub";    //公钥路径

    private static final String priKeyPath = "F:/heima/rsa/rsa.pri";    //私钥路径
    private PublicKey publicKey;    //公钥

    private PrivateKey privateKey;  //私钥

    @Test
    public void testRsa() throws Exception {    //生成公钥、私钥
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 50);
        System.out.println(token);
    }


    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU1MzU5MDM0OX0.XcXkqsKtyLvor3EmNuXodTOtiO_R0YLoRREUq_8iy16pCMRmqLbOVSGF9wwZYlGa7WUpfnw0grJlQLlqxzwe81Bcq2z4eJNKqO2cHJ7J6RzQhPG6I7DRVI97gF_PHmv7Dlj8ISd1Tl4TZHfA0v7aIlyinrahUdU5y0305HtAryw";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}


