package com.ogic.spikesystemsqlservice.controller;

import com.ogic.spikesystemapi.entity.UserEntity;
import com.ogic.spikesystemsqlservice.annotation.Master;
import com.ogic.spikesystemsqlservice.annotation.Slave;
import com.ogic.spikesystemsqlservice.mapper.UserMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author ogic
 */
@RestController
public class UserSqlController {

    @Resource
    UserMapper userMapper;

    @GetMapping("/sql/select/user/{username}")
    public Optional<UserEntity> getUser(@PathVariable String username){
        return Optional.ofNullable(userMapper.getUseByUsername(username));
    }

    @PostMapping("/sql/insert/user")
    public Optional<Integer> insertUser(@RequestParam UserEntity user){
        return Optional.ofNullable(userMapper.insertUser(user));
    }

}
