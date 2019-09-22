package com.ogic.spikesystemauthenticationservice.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ogic.spikesystemapi.entity.UserEntity;
import com.ogic.spikesystemauthenticationservice.component.TokenCreateUtil;
import com.ogic.spikesystemauthenticationservice.mapper.UserMapper;
import com.ogic.spikesystemauthenticationservice.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ogic
 */
@Service
public class AuthServiceImpl implements AuthService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    UserMapper userMapper;

    @Autowired
    TokenCreateUtil tokenCreateUtil;

    /** 一天 **/
    private Long tokenLife = 24L * 60 * 60 * 1000;

    @Override
    public String register(UserEntity userEntity) {

        Integer res = userMapper.insertUser(userEntity);
        if (res != null && res.equals(1)) {
            return "register success";
        }
        return "fail";
    }

    @Override
    public String login(String username, String password) {
        UserEntity user = userMapper.getUserByUsername(username);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return tokenCreateUtil.createToken(user, tokenLife);
            }
        }
        return "login fail";
    }

    @Override
    public UserEntity accInfo(String token) {
        DecodedJWT jwt = tokenCreateUtil.decodeToken(token);
        if (jwt != null){
            jwt = tokenCreateUtil.verifyToken(jwt);
            if (jwt != null){
                UserEntity user = userMapper.getUserByUsername(jwt.getClaim("username").asString());
                user.setPassword("");
                return user;
            }
        }
        return null;
    }


}

