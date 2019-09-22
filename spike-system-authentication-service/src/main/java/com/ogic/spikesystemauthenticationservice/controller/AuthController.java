package com.ogic.spikesystemauthenticationservice.controller;

import com.ogic.spikesystemapi.entity.UserEntity;
import com.ogic.spikesystemauthenticationservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author ogic
 */
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public Optional<String> login(@RequestParam("username") final String username, @RequestParam final String password) {
        return Optional.ofNullable(authService.login(username,password));
    }

    /**
     * 注册
     * @param userEntity
     * @return
     */
    @PostMapping("/register")
    public Optional<String> register(@RequestBody final UserEntity userEntity){
        return Optional.ofNullable(authService.register(userEntity));
    }

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    @GetMapping("/accInfo")
    public Optional<UserEntity> accInfo(@RequestParam final String token){
        return Optional.ofNullable(authService.accInfo(token));
    }
}

