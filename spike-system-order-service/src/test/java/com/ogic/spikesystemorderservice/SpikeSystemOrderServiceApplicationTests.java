package com.ogic.spikesystemorderservice;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemorderservice.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpikeSystemOrderServiceApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    OrderService orderService;

    @Test
    public void contextLoads() {
        OrderEntity order = orderService.buy(20L, 1L, 200);
        System.out.println(order);
    }

}
