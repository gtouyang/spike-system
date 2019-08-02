package com.ogic.spikesystemauthenticationservice.controller;

import com.ogic.spikesystemauthenticationservice.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private UserService userService;

    @PostMapping("/token")
    public String getToken(@RequestParam("username") final String username, @RequestParam("password") final String password){
        String token= userService.login(username,password);
        if(StringUtils.isEmpty(token)){
            return "fail create token";
        }
        return token;
    }
}
