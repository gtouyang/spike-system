package com.ogic.spikesystemauthenticationservice.service;


import com.ogic.spikesystemapi.entity.UserEntity;

import java.util.Optional;

/**
 * @author ogic
 */
public interface AuthService {

    /**
     * 注册
     * @param userEntity    用户对象
     * @return  注册消息
     */
    String register(UserEntity userEntity);

    /**
     * 登录
     * @param username  用户名
     * @param password  密码
     * @return  token
     */
    String login(String username, String password);

    /**
     * 通过Token获得用户名
     * @param token 身份令牌
     * @return  用户名
     */
    Optional findByToken(String token);
}
