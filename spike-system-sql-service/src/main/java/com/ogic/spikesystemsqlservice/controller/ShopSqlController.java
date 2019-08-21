package com.ogic.spikesystemsqlservice.controller;

import com.ogic.spikesystemapi.entity.ShopEntity;
import com.ogic.spikesystemsqlservice.mapper.ShopMappper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@RestController
public class ShopSqlController {
    @Resource
    ShopMappper shopMappper;

    @GetMapping(value = "/sql/select/shop")
    public Optional<ShopEntity> getShopById(@RequestParam Long id) {
        return Optional.ofNullable(shopMappper.getShopById(id));
    }

    @GetMapping(value = "/sql/select/shop")
    public Optional<List<ShopEntity>> getShopByOwner(@RequestParam String owner) {
        return Optional.ofNullable(shopMappper.getShopsByOwner(owner));
    }

    @PostMapping(value = "/sql/insert/shop")
    public Optional<Integer> insertShop(@RequestParam ShopEntity shop) {
        return Optional.ofNullable(shopMappper.insertShop(shop));
    }

    @PutMapping(value = "/sql/update/shop")
    public Optional<Integer> updateShopMoney(@RequestParam ShopEntity shop) {
        return Optional.ofNullable(shopMappper.updateShopMoney(shop));
    }
}
