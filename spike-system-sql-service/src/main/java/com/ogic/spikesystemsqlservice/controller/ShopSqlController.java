package com.ogic.spikesystemsqlservice.controller;

import com.ogic.spikesystemapi.entity.ShopEntity;
import com.ogic.spikesystemsqlservice.mapper.ShopMapper;
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
    ShopMapper shopMapper;

    @GetMapping(value = "/sql/select/shop")
    public Optional<ShopEntity> getShopById(@RequestParam Long id) {
        return Optional.ofNullable(shopMapper.getShopById(id));
    }

    @GetMapping(value = "/sql/select/shops")
    public Optional<List<ShopEntity>> getShopByOwner(@RequestParam String owner) {
        return Optional.ofNullable(shopMapper.getShopsByOwner(owner));
    }

    @PostMapping(value = "/sql/insert/shop")
    public Optional<Integer> insertShop(@RequestBody ShopEntity shop) {
        return Optional.ofNullable(shopMapper.insertShop(shop));
    }

    @PutMapping(value = "/sql/update/shop")
    public Optional<Integer> updateShopMoney(@RequestBody ShopEntity shop) {
        return Optional.ofNullable(shopMapper.updateShopMoney(shop));
    }
}
