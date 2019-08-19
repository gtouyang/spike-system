package com.ogic.spikesystemsqlservice;

import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemsqlservice.mapper.ProductMapper;
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

    @Test
    public void contextLoads() {
        ProductEntity product = new ProductEntity().setName("iphone").setOriginPrice(10000.00).setAmount(500);
        productMapper.insertProduct(product);
    }

}
