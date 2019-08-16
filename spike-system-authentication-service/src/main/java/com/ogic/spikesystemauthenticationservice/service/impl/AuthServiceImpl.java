package com.ogic.spikesystemauthenticationservice.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ogic.spikesystemapi.entity.UserEntity;
import com.ogic.spikesystemapi.service.UserSqlExposeService;
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
    UserSqlExposeService userSqlExposeService;

    @Autowired
    TokenCreateUtil tokenCreateUtil;

    Long tokenLife = 86400L;

    @Override
    public Optional<String> register(UserEntity userEntity) {

        Optional<Integer> res = userSqlExposeService.insertUser(userEntity);
        if (res.isPresent() && res.get() > 0){
            return Optional.of("create user success");
        }
        return Optional.of("fail");
    }

    @Override
    public Optional<String> login(String username, String password) {
        Optional<UserEntity> userOptional = userSqlExposeService.getUser(username);
        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            if (userEntity.getPassword().equals(password)) {
                return Optional.ofNullable(tokenCreateUtil.createToken(userEntity, tokenLife));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional findByToken(String token) {
        DecodedJWT jwt = tokenCreateUtil.decodeToken(token);
        if (jwt != null) {

            String username = jwt.getClaim("username").asString();
            return Optional
                    .ofNullable(username)
                    .map(String::valueOf)
                    .flatMap(userSqlExposeService::getUser);
        }
        return  Optional.empty();
    }
}

