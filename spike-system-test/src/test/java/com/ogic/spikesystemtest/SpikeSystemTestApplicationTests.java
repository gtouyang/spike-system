package com.ogic.spikesystemtest;

import com.ogic.spikesystemapi.common.TransformUtil;
import com.ogic.spikesystemapi.entity.*;
import com.ogic.spikesystemapi.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpikeSystemTestApplicationTests {

    @Autowired
    AuthExposeService authExposeService;

    @Autowired
    PayExposeService payExposeService;

    @Autowired
    ShopExposeService shopExposeService;

    @Autowired
    GoodExposeService goodExposeService;

    @Autowired
    OrderQueryExposeService orderQueryExposeService;

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Test
    public void addDefaultUser(){
        UserEntity user = new UserEntity()
                .setUsername("ud")
                .setPassword("ud-pass")
                .setEmail("ud@england.com");
        authExposeService.register(user);

        user = new UserEntity()
                .setUsername("tiny")
                .setPassword("tiny-pass")
                .setEmail("tiny@wangyi.com");
        authExposeService.register(user);
    }

    @Test
    public void addDefaultWallet(){
        WalletEntity wallet = new WalletEntity()
                .setUsername("tiny")
                .setMoney(1000000.0)
                .setPayPassword("tiny-pay");
        Optional res = payExposeService.addWallet(wallet);
        if (res.isPresent()){
            System.out.println(res.get());
        }
    }

    @Test
    public void addDefaultShop(){
        ShopEntity shop = new ShopEntity()
                .setOwner("ud")
                .setShopName("ud-shop");
        Optional res = shopExposeService.createShop(shop);
        if (res.isPresent()){
            System.out.println(res.get());
        }
    }

    @Test
    public void addDefaultGoods(){
        GoodEntity normal = new GoodEntity()
                .setName("没有秒杀价格的商品")
                .setInfo("此商品一直按原价付款")
                .setOriginPrice(1.0)
                .setAmount(1000)
                .setShopId(1);
        Optional res = goodExposeService.addGood(normal);
        if (res.isPresent()){
            System.out.println(res.get());
        }
    }

    @Test
    public void getGood(){
        Optional res = goodExposeService.getGoodById(1);
        if (res.isPresent()){
            System.out.println(res.get());
        }
    }

    @Test
    public void orderGoods(){
        OrderEntity order = new OrderEntity()
                .setOrderUsername("tiny")
                .setGoodId(1)
                .setAmount(100);
        kafkaTemplate.send("waitingOrder", order);
        System.out.println(order);
    }

    @Test
    public void payOrder(){
        long orderId = 2019091815593081157l;
        Optional<OrderEntity> orderOptional = orderQueryExposeService.getOrder(orderId);
        Transaction transaction = new Transaction(orderOptional.get(), 1, "tiny-pay");
        kafkaTemplate.send("payingOrder", transaction);
    }

}
