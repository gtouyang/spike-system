package com.ogic.spikesystempayservice.service.impl;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.entity.WalletEntity;
import com.ogic.spikesystempayservice.mapper.WalletMapper;
import com.ogic.spikesystempayservice.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


/**
 * @author ogic
 */
@Service
public class PayServiceImpl implements PayService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Resource
    WalletMapper walletMapper;

    @Override
    public List<WalletEntity> getUserAllWallets(String username) {
        List<WalletEntity> walletEntityList = walletMapper.getWalletByUsername(username);
        if (walletEntityList == null || walletEntityList.size() < 1) {
            return null;
        }
        walletEntityList.sort(Comparator.comparing(WalletEntity::getMoney));
        return walletEntityList;
    }

    @Override
    public List<WalletEntity> getUserAllWallets(String username, long orderId) {
        OrderEntity order = (OrderEntity) redisTemplate.boundValueOps(Long.toString(orderId)).get();
        if (order == null) {
            return null;
        }

        List<WalletEntity> walletEntityList = walletMapper.getWalletByUsername(username);
        if (walletEntityList == null || walletEntityList.size() < 1) {
            return null;
        }

        for (WalletEntity wallet : walletEntityList) {
            if (wallet.getMoney() < order.getPayMoney()){
                walletEntityList.remove(wallet);
            }
        }
        walletEntityList.sort(Comparator.comparing(WalletEntity::getMoney));
        return walletEntityList;
    }


    @Override
    public Integer addWallet(WalletEntity wallet) {
        return walletMapper.insertWallet(wallet);
    }
}
