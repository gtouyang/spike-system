package com.ogic.spikesystemapi.service;

import com.ogic.spikesystemapi.entity.GoodEntity;
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
    @GetMapping(value = "/sql/select/good/{id}")
    Optional<GoodEntity> getGoodById(@PathVariable Long id);

    /**
     * 根据在数据库的位置查询商品
     * @param offest
     * @param rows
     * @return
     */
    @GetMapping(value = "/sql/select/goods")
    Optional<List<GoodEntity>> getGoods(@RequestParam Long offest, @RequestParam Integer rows);

    /**
     * 插入新商品
     * @param good
     * @return
     */
    @PostMapping(value = "/sql/insert/good")
    Optional<Integer> insertGood(@RequestBody GoodEntity good);

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
    Optional<Integer> insertUser(@RequestBody UserEntity user);

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
    Optional<Integer> insertWallet(@RequestBody WalletEntity wallet);

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
    @GetMapping(value = "/sql/select/shops")
    Optional<List<ShopEntity>> getShopByOwner(@RequestParam String owner);

    /**
     * 插入新商铺
     * @param shop
     * @return
     */
    @PostMapping(value = "/sql/insert/shop")
    Optional<Integer> insertShop(@RequestBody ShopEntity shop);

    /**
     * 更新商铺余额
     * @param shop
     * @return
     */
    @PutMapping(value = "/sql/update/shop")
    Optional<Integer> updateShopMoney(@RequestBody ShopEntity shop);
}
