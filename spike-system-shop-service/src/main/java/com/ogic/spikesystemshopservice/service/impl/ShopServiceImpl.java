package com.ogic.spikesystemshopservice.service.impl;

import com.ogic.spikesystemapi.entity.ShopEntity;
import com.ogic.spikesystemapi.service.SqlExposeService;
import com.ogic.spikesystemshopservice.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
public class ShopServiceImpl implements ShopService {

    @Autowired
    SqlExposeService sqlExposeService;

    @Override
    public Optional<ShopEntity> getShopById(Long id) {
        return sqlExposeService.getShopById(id);
    }

    @Override
    public Optional<List<ShopEntity>> getShopByOwner(String owner) {
        return sqlExposeService.getShopByOwner(owner);
    }

    @Override
    public Optional<ShopEntity> createShop(ShopEntity shop) {
        if (shop == null || shop.getOwner() == null || shop.getShopName() == null) {
            return Optional.of(new ShopEntity().setShopName("undefined"));
        }
        Optional<Integer> res = sqlExposeService.insertShop(shop);
        if (res.isPresent() && res.get() == 1) {
            Optional<List<ShopEntity>> shopListOptional = sqlExposeService.getShopByOwner(shop.owner);
            if (shopListOptional.isPresent()) {
                List<ShopEntity> shopList = shopListOptional.get();
                if (shopList.size() >= 1) {
                    for (ShopEntity entity : shopList) {
                        if (entity.getShopName().equals(shop.getShopName())) {
                            return Optional.of(entity);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<ShopEntity> receiveMoney(Long id, Double money) {
        Optional<ShopEntity> shop = getShopById(id);
        if (shop.isPresent()) {
            shop.get().setMoney(shop.get().getMoney() + money);
            Optional<Integer> res = sqlExposeService.updateShopMoney(shop.get());
            if (res.isPresent() && res.get() == 1) {
                return shop;
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<ShopEntity> deductMoney(Long id, Double money) {
        Optional<ShopEntity> shop = getShopById(id);
        if (shop.isPresent() && shop.get().getMoney() >= money) {
            shop.get().setMoney(shop.get().getMoney() - money);
            Optional<Integer> res = sqlExposeService.updateShopMoney(shop.get());
            if (res.isPresent() && res.get() == 1) {
                return shop;
            }
        }
        return Optional.empty();
    }
}
