package com.ogic.spikesystemauthenticationservice.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ogic.spikesystemapi.entity.UserEntity;
import com.ogic.spikesystemapi.service.SqlExposeService;
import com.ogic.spikesystemauthenticationservice.component.TokenCreateUtil;
import com.ogic.spikesystemauthenticationservice.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author ogic
 */
@Service
public class AuthServiceImpl implements AuthService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SqlExposeService sqlExposeService;

    @Autowired
    TokenCreateUtil tokenCreateUtil;

    /** 一天 **/
    private Long tokenLife = 24L * 60 * 60 * 1000;

    @Override
    public Optional<String> register(UserEntity userEntity) {

        Optional<Integer> res = sqlExposeService.insertUser(userEntity);
        if (res.isPresent() && res.get() > 0){
            return Optional.of("create user success");
        }
        return Optional.of("fail");
    }

    @Override
    public Optional<String> login(String username, String password) {
        Optional<UserEntity> userOptional = sqlExposeService.getUser(username);
        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            if (userEntity.getPassword().equals(password)) {
                return Optional.ofNullable(tokenCreateUtil.createToken(userEntity, tokenLife));
            }
        }
        return Optional.of("login fail");
    }

    @Override
    public Optional findByToken(String token) {
        DecodedJWT jwt = tokenCreateUtil.decodeToken(token);
        if (jwt != null) {

            String username = jwt.getClaim("username").asString();
            return Optional
                    .ofNullable(username)
                    .map(String::valueOf)
                    .flatMap(sqlExposeService::getUser);
        }
        return  Optional.empty();
    }
}

