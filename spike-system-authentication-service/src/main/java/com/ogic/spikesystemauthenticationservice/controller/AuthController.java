package com.ogic.spikesystemauthenticationservice.controller;

import com.ogic.spikesystemapi.entity.UserEntity;
import com.ogic.spikesystemauthenticationservice.service.AuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ogic
 */
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public String login(@RequestParam("username") final String username, @RequestParam("password") final String password) {
        String token= authService.login(username,password);
        if(StringUtils.isEmpty(token)){
            return "fail create token";
        }
        return token;
    }

    @PutMapping("/register")
    public String register(@RequestParam("user") final UserEntity userEntity){
        return authService.register(userEntity);
    }
}

