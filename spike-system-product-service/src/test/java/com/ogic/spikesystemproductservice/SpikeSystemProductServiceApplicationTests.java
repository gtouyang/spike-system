package com.ogic.spikesystemproductservice;

import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemproductservice.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpikeSystemProductServiceApplicationTests {

    @Autowired
    ProductService productService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void insertProductData(){
        for (int i = 0; i < 50; i++) {
            ProductEntity product = new ProductEntity()
                    .setName("testProduct"+i)
                    .setAmount(1000)
                    .setOriginPrice(10.0+i)
                    .setSpikePrice(5.0+i)
                    .setSpikeStartTime(new Date(System.currentTimeMillis()+3600))
                    .setSpikeEndTime(new Date(System.currentTimeMillis()+7200))
                    .setImageUrl(null)
                    .setInfo("测试用");

            productService.addProduct(product);
        }
    }

}
