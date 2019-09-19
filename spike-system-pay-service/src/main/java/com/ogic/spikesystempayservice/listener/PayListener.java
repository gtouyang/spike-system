package com.ogic.spikesystempayservice.listener;

import com.ogic.spikesystemapi.common.TransformUtil;
import com.ogic.spikesystemapi.entity.*;
import com.ogic.spikesystemapi.service.GoodExposeService;
import com.ogic.spikesystemapi.service.ShopExposeService;
import com.ogic.spikesystempayservice.mapper.OrderMapper;
import com.ogic.spikesystempayservice.mapper.WalletMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @author ogic
 */
@Component
public class PayListener {

    @Resource
    WalletMapper walletMapper;

    @Resource
    OrderMapper orderMapper;

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    GoodExposeService goodExposeService;

    @Autowired
    ShopExposeService shopExposeService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(id = "pay-service", topics = {"payingOrder"})
    public void pay(Object data){

        logger.info("receive data: " + data);
        ConsumerRecord record = (ConsumerRecord) data;
        Transaction transaction = (Transaction) record.value();
        OrderEntity order = transaction.getOrder();
        Long walletId = transaction.getPayWalletId();
        String payPassword = transaction.getPayPassword();
        WalletEntity wallet = walletMapper.getWalletById(walletId);

        logger.info("transform success: " + transaction.toString());

        /* 如果密码正确切余额充足 */
        if (payPassword.equals(wallet.getPayPassword()) && wallet.getMoney() >= order.getPayMoney()){
            logger.info("update wallet[" + wallet.getId() + "] money");
            walletMapper.updateWalletMoney(walletId, wallet.getMoney(), wallet.getVersion());
            order.setPayWalletId(walletId);
            order.setPayTime(new Date());
            order.setOrderStatus(OrderEntity.OrderStatusEnum.FINISHED.getStatus());
            logger.info("finish order[" + order.getId() + "]");

            /* 完成订单 */
            finishOrder(order);

            /* 将订单从redis中移除 */
            deleteOrderOnRedis(order.getId());

            /* 把钱转给商铺 */
            sendMoneyToShop(order.getGoodId(), order.getPayMoney());
        }
    }

    @Async
    public void deleteOrderOnRedis(long orderId){
        redisTemplate.delete(Long.toString(orderId));
    }

    @Async
    public void sendMoneyToShop(long goodId,  double money){
        Optional<GoodEntity> goodOptional = goodExposeService.getGoodById(goodId);
        if (goodOptional.isPresent()){
            Optional<ShopEntity> shopOptional = shopExposeService.receiveMoney(goodOptional.get().getShopId(), money);
            if (! shopOptional.isPresent()){
                logger.info("can not find shop[" + goodOptional.get().getShopId() + "]");
            }
        }

    }

    @Async
    public void finishOrder(OrderEntity order){

        /* 订单已在DB里 */
        if (orderMapper.updateOrder(order) != 1){

            /* 订单未在DB里 */
            if (orderMapper.insertOrder(order) != 1){

                /* 订单在update前还未存在，insert时就存在了 */
                orderMapper.updateOrder(order);
            }
        }
    }
}
