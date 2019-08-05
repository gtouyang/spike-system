package com.ogic.spikesystemauthenticationservice.mapper;

import com.ogic.spikesystemapi.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author ogic
 */
@Mapper
public interface UserMapper {


    /**
     * 根据用户名获取用户实例
     * @param username  用户名
     * @return  用户对象
     */
    @Select("select * from user_basic_info where username like #{username}")
    Optional<UserEntity> findUserBasicInfoByUsername(String username);

    /**
     * 插入User
     * @param userEntity    用户对象
     * @return  结果
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into user_basic_info(username, password, email)" +
            "values(" +
            "#{username}," +
            "#{password}," +
            "#{email}" +
            ");")
    int insertUserBasicInfo(UserEntity userEntity);
}
