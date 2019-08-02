package com.ogic.spikesystemauthenticationservice.service;

import com.ogic.spikesystemapi.entity.UserEntity;

/**
 * @author ogic
 */
public interface AuthService {
    /**
     * 注册
     * @param userToAdd 要注册的账户
     * @return
     */
    UserEntity register(UserEntity userToAdd);
    String login(String username, String password);
    String refresh(String oldToken);
}