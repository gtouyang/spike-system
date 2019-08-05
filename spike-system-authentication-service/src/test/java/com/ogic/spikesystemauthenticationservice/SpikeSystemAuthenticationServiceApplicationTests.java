package com.ogic.spikesystemauthenticationservice;

import com.ogic.spikesystemapi.entity.UserEntity;
import com.ogic.spikesystemauthenticationservice.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLIntegrityConstraintViolationException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpikeSystemAuthenticationServiceApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    public void contextLoads() {
        System.out.println(userMapper.findUserBasicInfoByUsername("ha"));
        UserEntity userEntity = new UserEntity()
                .setUsername("haafafaassa")
                .setPassword("haha")
                .setEmail("ha@ah");
        try {
            System.out.println(userMapper.insertUserBasicInfo(userEntity));
        }catch (DataAccessException exception){
            exception.printStackTrace();
            String cause = exception.getCause().toString();
            if (cause.contains("Duplicate")) {
                System.out.println("username exists");
            }
        }
    }

}
