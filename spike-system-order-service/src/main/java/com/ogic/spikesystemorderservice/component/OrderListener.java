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
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author ogic
 */
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
            if (order.getOrderUsername() != null) {
                if (calculateMoney(order) && tryReduceAmount(order)){
                    logger.info("ready order : [" + order.getId() + "]");
                    kafkaTemplate.send("readyOrder", order);
                }else {
                    logger.info("fail order : [" + order.getId() + "]-->" + order.getInfo());
                    kafkaTemplate.send("canceledOrder", order);
                }
                redisTemplate.boundValueOps(Long.toString(order.getId())).setIfAbsent(order, 15, TimeUnit.MINUTES);
            } else {
                logger.info("bad order: [" + order.getId() + "]");
            }
        }
    }

    /**
     * 检查并减少库存
     * @param order
     * @return
     */
    private boolean tryReduceAmount(OrderEntity order) {
        order.setOrderTime(new Date());
        if (guavaCache.contain(order.getGoodId())){
            order.setOrderStatus(OrderEntity.OrderStatusEnum.CANCELED)
                    .setInfo("库存不足");
            return false;
        }

        if (decrementAmountInRedis(order.getGoodId(), order.getAmount()) < 0){
            incrementAmountInRedis(order.getGoodId(), order.getAmount());
            guavaCache.add(order.getGoodId());
            logger.debug("set good[" + order.getGoodId() + "] disabled");
            order.setOrderStatus(OrderEntity.OrderStatusEnum.CANCELED)
                    .setInfo("库存不足");
            return false;
        }

        if (amountMapper.reduceAmount(order.getGoodId(), order.getAmount()) != 1){
            guavaCache.add(order.getGoodId());
            logger.debug("set good[" + order.getGoodId() + "] disabled");
            order.setOrderStatus(OrderEntity.OrderStatusEnum.CANCELED)
                    .setInfo("库存不足");
            return false;
        }
        logger.debug("order[ " + order.getId() + "]-->good[" + order.getGoodId() + "] is enough");
        order.setOrderStatus(OrderEntity.OrderStatusEnum.READY);
        return true;
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
        logger.debug("rollback amount of good[" + goodId + "] in redis");
        redisTemplate.boundHashOps("amount").increment(goodId, amount);
    }

    @Async
    public void incrementAmountInDatabase(Long goodId, Integer amount){
        logger.debug("rollback amount of good[" + goodId + "] in Database");
        amountMapper.addAmount(goodId, amount);
    }


    /**
     * 计算订单商品价格
     * @param order
     * @return
     */
    private boolean calculateMoney(OrderEntity order){
        Date current = order.getOrderTime();
        Optional<GoodEntity> goodOptional = goodExposeService.getGoodById(order.getGoodId());
        if (goodOptional.isPresent()){
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
            return false;
        }
        logger.info("calculate order[" + order.getId() + "] money success");
        return true;
    }
}
