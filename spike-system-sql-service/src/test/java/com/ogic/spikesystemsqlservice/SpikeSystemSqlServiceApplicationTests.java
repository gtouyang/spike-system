package com.ogic.spikesystemsqlservice;

import com.ogic.spikesystemsqlservice.annotation.Slave;
import com.ogic.spikesystemsqlservice.mapper.ProductMapper;
import com.ogic.spikesystemsqlservice.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpikeSystemSqlServiceApplicationTests {

    @Resource
    ProductMapper productMapper;

    @Resource
    UserMapper userMapper;

    @Slave
    @Test
    public void contextLoads() {
        System.out.println(userMapper.getUseByUsername("ogic"));
    }

}
