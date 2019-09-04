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
import java.util.Optional;

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
    public Optional<String> register(UserEntity userEntity) {

        Integer res = userMapper.insertUser(userEntity);
        if (res != null && res.equals(1)) {
            return Optional.of("register success");
        }
        return Optional.of("fail");
    }

    @Override
    public Optional<String> login(String username, String password) {
        UserEntity user = userMapper.getUseByUsername(username);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return Optional.ofNullable(tokenCreateUtil.createToken(user, tokenLife));
            }
        }
        return Optional.of("login fail");
    }

    @Override
    public Optional<String> findByToken(String token) {
        DecodedJWT jwt = tokenCreateUtil.decodeToken(token);
        if (jwt != null) {

            String username = jwt.getClaim("username").asString();
            return Optional
                    .ofNullable(username)
                    .map(String::valueOf)
                    .map(userMapper::getUseByUsername)
                    .map(UserEntity::toString);
        }
        return  Optional.empty();
    }
}

