package com.ogic.spikesystempayservice.service.impl;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.entity.WalletEntity;
import com.ogic.spikesystempayservice.mapper.WalletMapper;
import com.ogic.spikesystempayservice.service.PayService;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author ogic
 */
public class PayServiceImpl implements PayService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    WalletMapper walletMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 更新钱包余额
     * @param order 订单
     * @param walletId  钱包ID
     * @return  完成情况
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean reduceMoney(Long walletId,String payPassword, OrderEntity order){
        WalletEntity wallet = walletMapper.getWalletById(walletId);
        if (wallet.getMoney() < order.getPayMoney() || !wallet.getPayPassword().equals(payPassword)){
            return false;
        }
        return walletMapper.updateMoney(walletId,
                wallet.getMoney() - order.getPayMoney(),
                wallet.getVersion()) == 1;
    }

    @Async
    public void rollbackMoney(Long walletId, OrderEntity order){
        WalletEntity wallet;
        do{
            wallet = walletMapper.getWalletById(walletId);

        }while (walletMapper.updateMoney(walletId,
                wallet.getMoney()+order.getPayMoney(),
                wallet.getVersion()) != 1);
    }

    private String lockOrder(String orderId){
        if (redisTemplate.boundValueOps("lock"+orderId).get() != null){
            return null;
        }
        String lockCode = RandomStringUtils.randomAlphabetic(10);
        if (redisTemplate.boundValueOps("lock" + orderId).setIfAbsent(lockCode, 5, TimeUnit.MINUTES)){
            return lockCode;
        }
        return null;
    }


    @Override
    public List<WalletEntity> getUserAllWallets(String username) {
        List<WalletEntity> walletEntityList = walletMapper.getWalletByUsername(username);
        if (walletEntityList == null || walletEntityList.size() < 1){
            return null;
        }
        walletEntityList.sort(Comparator.comparing(WalletEntity::getMoney));
        return walletEntityList;
    }

    @Override
    public List<WalletEntity> getUserAllWallets(String username, String orderId) {
        OrderEntity order = (OrderEntity) redisTemplate.boundValueOps(orderId).get();
        if (order == null) {
            return null;
        }

        List<WalletEntity> walletEntityList = walletMapper.getWalletByUsername(username);
        if (walletEntityList == null || walletEntityList.size() < 1){
            return null;
        }

        for (WalletEntity wallet : walletEntityList){
            if (wallet.getMoney() < order.getPayMoney()){
                walletEntityList.remove(wallet);
            }
        }
        walletEntityList.sort(Comparator.comparing(WalletEntity::getMoney));
        return walletEntityList;
    }

    @Override
    public Boolean payOrderByWallet(Long walletID, String pasPassword, String orderId) {
        String lockCode = lockOrder(orderId);
        if (lockCode == null){
            return false;
        }
        OrderEntity order = (OrderEntity) redisTemplate.boundValueOps(orderId).get();
        if (reduceMoney(walletID, pasPassword, order)){
            if (redisTemplate.boundValueOps("lock" + orderId).get() == lockCode){
                order.setOrderStatus(OrderEntity.OrderStatusEnum.FINISHED);
                rabbitTemplate.convertAndSend("order", "finishedOrder", order);
                redisTemplate.delete("lock"+orderId);
                redisTemplate.delete(orderId);
                return true;
            }else {
               rollbackMoney(walletID, order);
            }
        }
        return false;
    }
}
