package com.ogic.spikesystemconsumer;

import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemapi.entity.ShopEntity;
import com.ogic.spikesystemapi.entity.UserEntity;
import com.ogic.spikesystemapi.entity.WalletEntity;
import com.ogic.spikesystemapi.service.AuthExposeService;
import com.ogic.spikesystemapi.service.PayExposeService;
import com.ogic.spikesystemapi.service.ProductExposeService;
import com.ogic.spikesystemapi.service.ShopExposeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpikeSystemConsumerApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    @Test
    public void addDefaultUser(AuthExposeService authExposeService) {
        UserEntity user = new UserEntity().setUsername("ogic").setPassword("pass").setEmail("ogic@zio");
        authExposeService.register(user);
    }

    @Autowired
    @Test
    public void addDefaultShop(ShopExposeService shopExposeService) {
        ShopEntity shop = new ShopEntity().setOwner("ogic").setShopName("apple");
        shopExposeService.createShop(shop);
    }

    @Autowired
    @Test
    public void addDefaultWallet(PayExposeService payExposeService) {
        WalletEntity wallet = new WalletEntity().setUsername("ogic").setPayPassword("pay").setMoney(100000.0);
    }

    @Test
    public void addDefaultProduct(ProductExposeService productExposeService) {
        ProductEntity product = new ProductEntity()
                .setName("iphone")
                .setOriginPrice(1000.0)
                .setShopId(1L)
                .setAmount(500)
                .setInfo("iPhone 上最大的超视网膜显示屏，性能出类拔萃的 A12 仿生，安全性更进一步的面容 ID，以及支持景深控制的突破性双镜头系统")
                .setImageUrl("https://www.apple.com/v/iphone/home/aa/images/overview/designed_for_everyone__c8htpc2hbjma_medium.jpg");
        productExposeService.addProduct(product);
    }

}
