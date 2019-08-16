package com.ogic.spikesystemapi.service;

import com.ogic.spikesystemapi.entity.UserEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * @author ogic
 */
@FeignClient(value = "SPIKE-SYSTEM-SQL-SERVICE")
public interface UserSqlExposeService {

    /**
     * 获取新用户
     * @param username  用户名
     * @return  用户实例
     */
    @GetMapping("/sql/select/user/{username}")
    Optional<UserEntity> getUser(@PathVariable String username);

    /**
     * 插入新用户到数据库中
     * @param user  用户实例
     * @return 插入结果
     */
    @PutMapping("sql/insert/user")
    Optional<Integer> insertUser(@RequestParam UserEntity user);
}
