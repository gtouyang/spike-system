package com.ogic.spikesystemshopservice.service.impl;

import com.ogic.spikesystemapi.entity.ShopEntity;
import com.ogic.spikesystemshopservice.mapper.ShopMapper;
import com.ogic.spikesystemshopservice.service.ShopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Resource
    ShopMapper shopMapper;

    @Override
    public Optional<ShopEntity> getShopById(Long id) {
        return Optional.ofNullable(shopMapper.getShopById(id));
    }

    @Override
    public Optional<List<ShopEntity>> getShopByOwner(String owner) {
        return Optional.ofNullable(shopMapper.getShopsByOwner(owner));
    }

    @Override
    public Optional<ShopEntity> createShop(ShopEntity shop) {
        if (shop == null || shop.getOwner() == null || shop.getShopName() == null) {
            return Optional.of(new ShopEntity().setShopName("undefined"));
        }
        Integer res = shopMapper.insertShop(shop);
        if (res != null && res == 1) {
            List<ShopEntity> shopListOptional = shopMapper.getShopsByOwner(shop.owner);
            if (shopListOptional != null) {
                List<ShopEntity> shopList = shopListOptional;
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
            Integer res = shopMapper.updateShopMoney(shop.get());
            if (res != null && res.equals(1)) {
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
            Integer res = shopMapper.updateShopMoney(shop.get());
            if (res != null && res.equals(1)) {
                return shop;
            }
        }
        return Optional.empty();
    }
}
