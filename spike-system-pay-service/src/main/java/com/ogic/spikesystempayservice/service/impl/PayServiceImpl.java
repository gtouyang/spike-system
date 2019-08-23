package com.ogic.spikesystempayservice.service.impl;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.entity.WalletEntity;
import com.ogic.spikesystemapi.service.SqlExposeService;
import com.ogic.spikesystempayservice.service.PayService;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * @author ogic
 */
@Service
public class PayServiceImpl implements PayService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    SqlExposeService sqlExposeService;

    @Autowired
    RabbitTemplate rabbitTemplate;


    private final String LOCK_HEADER = "lock";

    private final long LOCK_MINUTES = 5;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 更新钱包余额
     * @param order 订单
     * @param walletId  钱包ID
     * @return  完成情况
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean reduceMoney(Long walletId,String payPassword, OrderEntity order){
        Optional<WalletEntity> wallet = sqlExposeService.getWalletById(walletId);
        if (wallet.isPresent()) {
            if (wallet.get().getMoney() < order.getPayMoney() || !wallet.get().getPayPassword().equals(payPassword)) {
                return false;
            }
            Optional result = sqlExposeService.updateWalletMoney(walletId,
                    wallet.get().getMoney() - order.getPayMoney(),
                    wallet.get().getVersion());
            if (result.isPresent()){
                return result.get().equals(1);
            }
        }
        return false;
    }

    @Async
    public void rollbackMoney(Long walletId, OrderEntity order) {
        while (true) {
            Optional<WalletEntity> wallet = sqlExposeService.getWalletById(walletId);
            Optional result;
            if (wallet.isPresent()) {
                result = sqlExposeService.updateWalletMoney(walletId,
                        wallet.get().getMoney() + order.getPayMoney(),
                        wallet.get().getVersion());
                if (result.isPresent() && result.get().equals(1)){
                    break;
                }
            }else {
                rabbitTemplate.convertAndSend("error", "rollback money", order);
            }
        }
    }

    private String lockOrder(String orderId){

        if (redisTemplate.boundValueOps(LOCK_HEADER + orderId).get() != null) {
            return null;
        }
        String lockCode = RandomStringUtils.randomAlphabetic(10);

        try {
            if (redisTemplate.boundValueOps(LOCK_HEADER + orderId).setIfAbsent(lockCode, LOCK_MINUTES, TimeUnit.MINUTES)) {
                return lockCode;
            }
        } catch (NullPointerException e) {
            logger.error(e.getMessage());
        }
        return null;
    }


    @Override
    public Optional<List<WalletEntity>> getUserAllWallets(String username) {
        Optional<List<WalletEntity>> walletEntityList = sqlExposeService.getWalletByUsername(username);
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

        Optional<List<WalletEntity>> walletEntityList = sqlExposeService.getWalletByUsername(username);
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
    public Optional<Boolean> payOrderByWallet(Long walletId, String payPassword, String orderId) {
        String lockCode = lockOrder(orderId);
        if (lockCode == null){
            return Optional.of(Boolean.FALSE);
        }
        OrderEntity order = (OrderEntity) redisTemplate.boundValueOps(orderId).get();
        if (reduceMoney(walletId, payPassword, order)) {
            if (redisTemplate.boundValueOps(LOCK_HEADER + orderId).get() == lockCode) {
                assert order != null;
                order.setOrderStatus(OrderEntity.OrderStatusEnum.FINISHED);
                rabbitTemplate.convertAndSend("order", "finishedOrder", order);
                redisTemplate.delete(LOCK_HEADER + orderId);
                redisTemplate.delete(orderId);
                return Optional.of(Boolean.TRUE);
            }else {
                rollbackMoney(walletId, order);
            }
        }
        return Optional.of(Boolean.FALSE);
    }

    @Override
    public Optional<Integer> addWallet(WalletEntity wallet) {
        return sqlExposeService.insertWallet(wallet);
    }
}
