package com.ogic.spikesystemauthenticationservice.controller;

import com.ogic.spikesystemapi.entity.UserEntity;
import com.ogic.spikesystemauthenticationservice.service.AuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * @author ogic
 */
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Optional<String> login(@RequestParam("username") final String username, @RequestBody final String password) {
        return authService.login(username,password);
    }

    @PutMapping("/register")
    public Optional<String> register(@RequestBody final UserEntity userEntity){
        return authService.register(userEntity);
    }
}

