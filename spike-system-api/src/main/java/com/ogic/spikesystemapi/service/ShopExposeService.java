package com.ogic.spikesystemapi.service;

import com.ogic.spikesystemapi.entity.ShopEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@FeignClient(value = "SPIKE-SYSTEM-SHOP-SERVICE")
public interface ShopExposeService {

    /**
     * 通过商铺ID获得商铺
     *
     * @param id
     * @return
     */
    @GetMapping("/shop")
    Optional<ShopEntity> getShopById(@RequestParam long id);

    /**
     * 通过卖家用户名获得商铺列表
     *
     * @param owner
     * @return
     */
    @GetMapping("/shops/{owner}")
    Optional<List<ShopEntity>> getShopByOwner(@RequestParam String owner);

    /**
     * 创建商铺
     *
     * @param shop
     * @return
     */
    @PostMapping("/shop")
    Optional<ShopEntity> createShop(@RequestBody ShopEntity shop);

    /**
     * 商铺收钱
     *
     * @param shopId
     * @param money
     * @return
     */
    @PutMapping("/shop/receive")
    Optional<ShopEntity> receiveMoney(@RequestParam long shopId, @RequestParam double money);

    /**
     * 商铺扣钱
     *
     * @param shopId
     * @param money
     * @return
     */
    @PutMapping("/shop/deduct")
    Optional<ShopEntity> deductMoney(@RequestParam long shopId, @RequestParam double money);
}
