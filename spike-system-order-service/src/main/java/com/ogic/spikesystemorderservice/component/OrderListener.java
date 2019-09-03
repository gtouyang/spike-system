package com.ogic.spikesystemorderservice.component;

import com.ogic.spikesystemapi.entity.GoodEntity;
import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.service.GoodExposeService;
import com.ogic.spikesystemorderservice.mapper.AmountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author ogic
 */
@Component
public class OrderListener {

    @Resource
    AmountMapper amountMapper;

    @Autowired
    GuavaCache guavaCache;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    GoodExposeService goodExposeService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @KafkaListener(id = "order-service", topics = {"waitingOrder"})
    public void listenTopicOrder(Object data){
        OrderEntity order = (OrderEntity) data;
        if (order!=null) {
            if (order.getGoodId() != null && order.getOrderUsername() != null && order.getAmount() != null) {
                tryCreateOrder(order);
                logger.info("finish order : [" + order.toString() + "]");
                redisTemplate.boundListOps(order.getOrderUsername() + "-OrderResult").rightPush(order);
            } else {
                logger.error("bad order: [" + order.toString() + "]");
                redisTemplate.boundListOps("badOrder").rightPush(order);
            }
        }
    }

    private void tryCreateOrder(OrderEntity order) {
        order.setOrderTime(new Date());
        if (guavaCache.contain(order.getGoodId())){
            order.setOrderStatus(OrderEntity.OrderStatusEnum.CANCELED)
                    .setInfo("库存不足");
            return;
        }

        if (decrementAmountInRedis(order.getGoodId(), order.getAmount()) < 0){
            incrementAmountInRedis(order.getGoodId(), order.getAmount());
            guavaCache.add(order.getGoodId());
            logger.debug("set good[" + order.getGoodId() + "] disabled");
            order.setOrderStatus(OrderEntity.OrderStatusEnum.CANCELED)
                    .setInfo("库存不足");
            return;
        }

        if (amountMapper.reduceAmount(order.getGoodId(), order.getAmount()) != 1){
            guavaCache.add(order.getGoodId());
            logger.debug("set good[" + order.getGoodId() + "] disabled");
            order.setOrderStatus(OrderEntity.OrderStatusEnum.CANCELED)
                    .setInfo("库存不足");
            return;
        }
        createOrder(order);
        logger.debug("good[" + order.getGoodId() + "] is enough");
        kafkaTemplate.send("readyOrder", order);
    }

    private Integer decrementAmountInRedis(Long goodId, Integer amount){
        Long res = redisTemplate.boundHashOps("amount").increment(goodId, -amount);
        logger.debug("amount of good[" + goodId + "] is " + res + " after decrement");
        if(res != null) {
            return Math.toIntExact(res);
        }else {
            return 0;
        }
    }


    @Async
    public void incrementAmountInRedis(Long goodId, Integer amount){
        logger.debug("rollback amount of good[" + goodId + "]");
        redisTemplate.boundHashOps("amount").increment(goodId, amount);
    }


    private void createOrder(OrderEntity order){
        Date current = order.getOrderTime();
        Optional<GoodEntity> goodOptional = goodExposeService.getGoodById(order.getGoodId());
        if (goodOptional.isPresent() && goodOptional.get().getOriginPrice() != null){
            if (goodOptional.get().getSpikeStartTime() != null
                    && goodOptional.get().getSpikeStartTime().before(current)
                    && goodOptional.get().getSpikeEndTime() != null
                    && goodOptional.get().getSpikeEndTime().after(current)
                    && goodOptional.get().getSpikePrice() != null){
                order.setPayMoney(goodOptional.get().getSpikePrice() * order.getAmount());
            }else {
                order.setPayMoney(goodOptional.get().getOriginPrice() * order.getAmount());
            }
        }else {
            order.setOrderStatus(OrderEntity.OrderStatusEnum.CANCELED);
            order.setInfo("获取商品价格出错");
        }

        DateFormat dateFormat = new SimpleDateFormat();
        do {
            order.setId(dateFormat.format(current) + randomNumbers(7));
            logger.debug("try add order[" + order.getId() + "]");
        } while (!redisTemplate.boundValueOps(order.getId()).setIfAbsent(order, 15, TimeUnit.MINUTES));
        logger.info("add order[" + order.getId() + "] success");
    }

    private String randomNumbers(int size) {
        StringBuilder builder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < size; i++) {
            builder.append(random.nextInt(9));
        }
        return builder.toString();
    }
}
