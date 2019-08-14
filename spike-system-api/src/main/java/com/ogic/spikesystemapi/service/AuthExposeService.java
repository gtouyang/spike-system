package com.ogic.spikesystemapi.service;

import com.ogic.spikesystemapi.entity.UserEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    Optional<String> login(@RequestParam("username") final String username, @RequestParam("password") final String password);

    /**
     * 注册服务
     * @param userEntity    用户实例
     * @return  注册结果
     */
    @PutMapping("/register")
    Optional<String> register(@RequestParam("user") final UserEntity userEntity);
}
