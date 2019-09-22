package com.ogic.spikesystemapi.service;

import com.ogic.spikesystemapi.entity.UserEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * @author ogic
 */
@FeignClient(value = "SPIKE-SYSTEM-AUTH-SERVICE")
public interface AuthExposeService {

    /**
     * 登录服务
     * @param username  用户名
     * @param password  密码
     * @return  token
     */
    @PostMapping("/login")
    Optional<String> login(@RequestParam("username") final String username, @RequestParam final String password);

    /**
     * 注册服务
     * @param userEntity    用户实例
     * @return  注册结果
     */
    @PostMapping("/register")
    Optional<String> register(@RequestBody final UserEntity userEntity);

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    @GetMapping("/accInfo")
    Optional<UserEntity> accInfo(@RequestParam final String token);
}
