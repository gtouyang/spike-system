package com.ogic.spikesystemshopservice.controller;

import com.ogic.spikesystemapi.entity.ShopEntity;
import com.ogic.spikesystemshopservice.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
public class ShopController {
    @Autowired
    ShopService shopService;

    @GetMapping("shop/{id}")
    public Optional<ShopEntity> getShopById(@PathVariable Long id) {
        return shopService.getShopById(id);
    }

    @GetMapping("/shops/{owner}")
    public Optional<List<ShopEntity>> getShopByOwner(@PathVariable String owner) {
        return shopService.getShopByOwner(owner);
    }

    @PostMapping("/shop")
    public Optional<ShopEntity> createShop(@RequestBody ShopEntity shop) {
        return shopService.createShop(shop);
    }

    @PutMapping("/shop/receive")
    public Optional<ShopEntity> receiveMoney(@RequestParam Long shopId, @RequestParam Double money) {
        return shopService.receiveMoney(shopId, money);
    }

    @PutMapping("/shop/deduct")
    public Optional<ShopEntity> deductMoney(@RequestParam Long shopId, @RequestParam Double money) {
        return shopService.deductMoney(shopId, money);
    }
}
