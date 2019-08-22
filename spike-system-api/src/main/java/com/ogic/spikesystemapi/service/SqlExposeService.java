package com.ogic.spikesystemapi.service;

import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemapi.entity.ShopEntity;
import com.ogic.spikesystemapi.entity.UserEntity;
import com.ogic.spikesystemapi.entity.WalletEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@FeignClient(value = "SPIKE-SYSTEM-SQL-SERVICE")
public interface SqlExposeService {
    /**
     * 根据ID查询商品
     * @param id
     * @return
     */
    @GetMapping(value = "/sql/select/product/{id}")
    Optional<ProductEntity> getProductById(@PathVariable Long id);

    /**
     * 根据在数据库的位置查询商品
     * @param offest
     * @param rows
     * @return
     */
    @GetMapping(value = "/sql/select/products")
    Optional<List<ProductEntity>> getProducts(@RequestParam Long offest, @RequestParam Integer rows);

    /**
     * 插入新商品
     * @param product
     * @return
     */
    @PostMapping(value = "/sql/insert/product")
    Optional<Integer> insertProduct(@RequestParam ProductEntity product);

    /**
     * 获取新用户
     * @param username  用户名
     * @return  用户实例
     */
    @GetMapping("/sql/select/user/{username}")
    Optional<UserEntity> getUser(@PathVariable String username);

    /**
     * 插入新用户到数据库中
     * @param user  用户实例
     * @return 插入结果
     */
    @PostMapping("/sql/insert/user")
    Optional<Integer> insertUser(@RequestParam UserEntity user);

    /**
     * 根据ID查询钱包
     * @param id
     * @return
     */
    @GetMapping(value = "/sql/select/wallet/{id}")
    Optional<WalletEntity> getWalletById(@PathVariable Long id);

    /**
     * 根据用户名获取钱包列表
     * @param username
     * @return
     */
    @GetMapping(value = "/sql/select/wallets/{username}")
    Optional<List<WalletEntity>> getWalletByUsername(@PathVariable String username);

    /**
     * 更新钱包余额
     * @param id
     * @param money
     * @param version
     * @return
     */
    @PutMapping(value = "/sql/update/wallet/money")
    Optional<Integer> updateWalletMoney(@RequestParam Long id, @RequestParam Double money, @RequestParam Integer version);

    /**
     * 插入新钱包
     * @param wallet
     * @return
     */
    @PostMapping(value = "/sql/insert/wallet")
    Optional<Integer> insertWallet(@RequestParam WalletEntity wallet);

    /**
     * 根据商铺ID获取商铺
     * @param id
     * @return
     */
    @GetMapping(value = "/sql/select/shop")
    Optional<ShopEntity> getShopById(@RequestParam Long id);

    /**
     * 根据商铺卖家用户名获取商铺列表
     * @param owner
     * @return
     */
    @GetMapping(value = "/sql/select/shop")
    Optional<List<ShopEntity>> getShopByOwner(@RequestParam String owner);

    /**
     * 插入新商铺
     * @param shop
     * @return
     */
    @PostMapping(value = "/sql/insert/shop")
    Optional<Integer> insertShop(@RequestParam ShopEntity shop);

    /**
     * 更新商铺余额
     * @param shop
     * @return
     */
    @PutMapping(value = "/sql/update/shop")
    Optional<Integer> updateShopMoney(@RequestParam ShopEntity shop);
}
