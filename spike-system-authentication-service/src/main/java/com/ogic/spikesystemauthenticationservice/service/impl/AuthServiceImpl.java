package com.ogic.spikesystemauthenticationservice.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ogic.spikesystemapi.entity.UserEntity;
import com.ogic.spikesystemauthenticationservice.component.TokenCreateUtil;
import com.ogic.spikesystemauthenticationservice.mapper.UserMapper;
import com.ogic.spikesystemauthenticationservice.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author ogic
 */
@Service
public class AuthServiceImpl implements AuthService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserMapper userMapper;

    @Autowired
    TokenCreateUtil tokenCreateUtil;

    @Value("token.life")
    Long tokenLife;

    @Override
    public String register(UserEntity userEntity) {
        try {
            int res = userMapper.insertUserBasicInfo(userEntity);
            if (res > 0){
                return "success";
            }
        }catch (DataAccessException exception){
            return "fail: username exists";
        }
        return "fail";
    }

    @Override
    public String login(String username, String password) {
        Optional userOptional = userMapper.findUserBasicInfoByUsername(username);
        if (userOptional.isPresent()) {
            UserEntity userEntity = (UserEntity) userOptional.get();
            return tokenCreateUtil.createToken(userEntity, tokenLife);
        }
        return null;
    }

    @Override
    public Optional findByToken(String token) {
        DecodedJWT jwt = tokenCreateUtil.decodeToken(token);
        if (jwt != null) {

            String username = jwt.getClaim("username").asString();
            return Optional
                    .ofNullable(username)
                    .map(String::valueOf)
                    .flatMap(userMapper::findUserBasicInfoByUsername);
        }
        return  Optional.empty();
    }
}

