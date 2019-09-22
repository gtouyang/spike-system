package com.ogic.spikesystemauthenticationservice.service;


import com.ogic.spikesystemapi.entity.UserEntity;

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
     * 获得用户信息
     * @param token
     * @return
     */
    UserEntity accInfo(String token);
}
