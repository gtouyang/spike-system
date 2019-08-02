package com.ogic.spikesystemauthenticationservice.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ogic.spikesystemapi.entity.UserEntity;
import com.ogic.spikesystemauthenticationservice.common.TokenCreateUtil;
import com.ogic.spikesystemauthenticationservice.entity.JwtUserFactory;
import com.ogic.spikesystemauthenticationservice.mapper.UserMapper;
import com.ogic.spikesystemauthenticationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ogic
 */
@Service
public class UserServiceImpl implements UserService {

//    @Autowired
//    UserMapper userMapper;

    @Autowired
    TokenCreateUtil tokenCreateUtil;

    @Override
    public String login(String username, String password) {
        UserEntity userEntity = new UserEntity()
                .setUsername(username)
                .setPassword(password);
        List<String> roles = new LinkedList<>();
        roles.add("user");
        UserDetails userDetails = new User(
                username,
                password,
                true,
                true,
                true,
                true,
                roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
        String token;
        token = tokenCreateUtil.createToken(userDetails, 3600L);
        return token;
    }

    @Override
    public Optional findByToken(String token) {
        DecodedJWT jwt = tokenCreateUtil.decodeToken(token);
        if (jwt != null) {
            String username = jwt.getClaim("username").asString();
            UserEntity userEntity = new UserEntity()
                    .setUsername(jwt.getClaim("username").asString())
                    .setPassword("demo");
            User user = new User(userEntity.getUsername(), userEntity.getPassword(), true, true, true, true,
                    AuthorityUtils.createAuthorityList("USER"));
            return Optional.of(user);
        }
        return  Optional.empty();
    }
}
