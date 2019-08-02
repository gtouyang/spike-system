package com.ogic.spikesystemauthenticationservice.controller;

import com.ogic.spikesystemapi.entity.UserEntity;
import com.ogic.spikesystemauthenticationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author ogic
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/api/users/hello",produces = "application/json")
    public String hello(){
        return "hello";
    }
}
