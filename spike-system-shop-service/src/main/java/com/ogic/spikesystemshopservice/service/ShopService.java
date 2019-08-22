package com.ogic.spikesystemshopservice.service;

import com.ogic.spikesystemapi.entity.ShopEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
public interface ShopService {

    /**
     * 根据商铺ID获取商铺
     *
     * @param id
     * @return
     */
    Optional<ShopEntity> getShopById(Long id);

    /**
     * 根据卖家名获取商铺
     *
     * @param owner
     * @return
     */
    Optional<List<ShopEntity>> getShopByOwner(String owner);

    /**
     * 创建商铺
     *
     * @param shop
     * @return
     */
    Optional<ShopEntity> createShop(ShopEntity shop);

    /**
     * 收钱
     *
     * @param id
     * @param money
     * @return
     */
    Optional<ShopEntity> receiveMoney(Long id, Double money);

    /**
     * 扣钱
     *
     * @param id
     * @param money
     * @return
     */
    Optional<ShopEntity> deductMoney(Long id, Double money);
}
