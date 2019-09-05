package com.ogic.spikesystempayservice.component;

import com.ogic.spikesystemapi.entity.GoodEntity;
import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.entity.ShopEntity;
import com.ogic.spikesystemapi.entity.WalletEntity;
import com.ogic.spikesystemapi.service.GoodExposeService;
import com.ogic.spikesystemapi.service.ShopExposeService;
import com.ogic.spikesystempayservice.mapper.WalletMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @author ogic
 */
public class PayListener {

    @Resource
    WalletMapper walletMapper;

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    GoodExposeService goodExposeService;

    @Autowired
    ShopExposeService shopExposeService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(id = "order-service", topics = {"waitingOrder"})
    public void pay(Object data){
        Map<String, Object> dataMap = (Map<String, Object>) data;
        OrderEntity order = (OrderEntity) dataMap.get("order");
        Long walletId = (Long) dataMap.get("walletId");
        String payPassword = (String) dataMap.get("payPassword");
        WalletEntity wallet = walletMapper.getWalletById(walletId);
        if (payPassword.equals(wallet.getPayPassword()) && wallet.getMoney() >= order.getPayMoney()){
            logger.info("update wallet[" + wallet.getId() + "] money");
            walletMapper.updateWalletMoney(walletId, wallet.getMoney(), wallet.getVersion());
            order.setPayWalletId(walletId);
            order.setPayTime(new Date());
            order.setOrderStatus(OrderEntity.OrderStatusEnum.FINISHED);
            logger.info("finish order[" + order.getId() + "]");
            kafkaTemplate.send("finishedOrder", order);
            deleteOrderOnRedis(order.getId());
            sendMoneyToShop(order.getGoodId(), order.getPayMoney());
        }
    }

    @Async
    public void deleteOrderOnRedis(long orderId){
        redisTemplate.delete(orderId);
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
}
