package com.ogic.spikesystemauthenticationservice.mapper;

import com.ogic.spikesystemapi.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

/**
 * @author ogic
 */
@Mapper
public interface UserMapper {


    /**
     * 根据用户名获取用户实例
     * @param username  用户名
     * @return  用户实例
     */
    @Select("select * from user where username like #{username}")
    Optional findByUsername(String username);

}
