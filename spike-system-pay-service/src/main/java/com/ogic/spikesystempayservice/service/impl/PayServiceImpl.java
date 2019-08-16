package com.ogic.spikesystempayservice.service.impl;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.entity.WalletEntity;
import com.ogic.spikesystemapi.service.WalletSqlExposeService;
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
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * @author ogic
 */
public class PayServiceImpl implements PayService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    WalletSqlExposeService walletSqlExposeService;

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
        Optional<WalletEntity> wallet = walletSqlExposeService.getWalletById(walletId);
        if (wallet.isPresent()) {
            if (wallet.get().getMoney() < order.getPayMoney() || !wallet.get().getPayPassword().equals(payPassword)) {
                return false;
            }
            Optional result = walletSqlExposeService.updateWalletMoney(walletId,
                    wallet.get().getMoney() - order.getPayMoney(),
                    wallet.get().getVersion());
            if (result.isPresent()){
                return result.get().equals(1);
            }
        }
        return false;
    }

    @Async
    public Boolean rollbackMoney(Long walletId, OrderEntity order){
        while (true) {
            Optional<WalletEntity> wallet = walletSqlExposeService.getWalletById(walletId);
            Optional result;
            if (wallet.isPresent()) {
                result = walletSqlExposeService.updateWalletMoney(walletId,
                        wallet.get().getMoney() + order.getPayMoney(),
                        wallet.get().getVersion());
                if (result.isPresent() && result.get().equals(1)){
                    return true;
                }
            }else {
                return false;
            }
        }
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
    public Optional<List<WalletEntity>> getUserAllWallets(String username) {
        Optional<List<WalletEntity>> walletEntityList = walletSqlExposeService.getWalletByUsername(username);
        if (!walletEntityList.isPresent() || walletEntityList.get().size() < 1){
            return Optional.empty();
        }
        walletEntityList.get().sort(Comparator.comparing(WalletEntity::getMoney));
        return walletEntityList;
    }

    @Override
    public Optional<List<WalletEntity>> getUserAllWallets(String username, String orderId) {
        OrderEntity order = (OrderEntity) redisTemplate.boundValueOps(orderId).get();
        if (order == null) {
            return Optional.empty();
        }

        Optional<List<WalletEntity>> walletEntityList = walletSqlExposeService.getWalletByUsername(username);
        if (!walletEntityList.isPresent() || walletEntityList.get().size() < 1){
            return Optional.empty();
        }

        for (WalletEntity wallet : walletEntityList.get()){
            if (wallet.getMoney() < order.getPayMoney()){
                walletEntityList.get().remove(wallet);
            }
        }
        walletEntityList.get().sort(Comparator.comparing(WalletEntity::getMoney));
        return walletEntityList;
    }

    @Override
    public Optional<Boolean> payOrderByWallet(Long walletID, String pasPassword, String orderId) {
        String lockCode = lockOrder(orderId);
        if (lockCode == null){
            return Optional.of(Boolean.FALSE);
        }
        OrderEntity order = (OrderEntity) redisTemplate.boundValueOps(orderId).get();
        if (reduceMoney(walletID, pasPassword, order)){
            if (redisTemplate.boundValueOps("lock" + orderId).get() == lockCode){
                order.setOrderStatus(OrderEntity.OrderStatusEnum.FINISHED);
                rabbitTemplate.convertAndSend("order", "finishedOrder", order);
                redisTemplate.delete("lock"+orderId);
                redisTemplate.delete(orderId);
                return Optional.of(Boolean.TRUE);
            }else {
               if (!rollbackMoney(walletID, order)){
                   rabbitTemplate.convertAndSend("error", "roolbackMoneyError", order);
               }
            }
        }
        return Optional.of(Boolean.FALSE);
    }
}
