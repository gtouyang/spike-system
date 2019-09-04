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

    @PostMapping("/login")
    public Optional<String> login(@RequestParam("username") final String username, @RequestParam final String password) {
        return authService.login(username,password);
    }

    @PostMapping("/register")
    public Optional<String> register(@RequestBody final UserEntity userEntity){
        return authService.register(userEntity);
    }
}

