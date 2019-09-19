package com.ogic.spikesystemorderservice.listener;

import com.ogic.spikesystemapi.entity.GoodEntity;
import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.service.GoodExposeService;
import com.ogic.spikesystemorderservice.component.GuavaCache;
import com.ogic.spikesystemorderservice.mapper.AmountMapper;
import com.ogic.spikesystemorderservice.mapper.OrderMapper;
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
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author ogic
 */
@Component
public class OrderListener {

    @Resource
    AmountMapper amountMapper;

    @Resource
    OrderMapper orderMapper;

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
        logger.info("receive data : " + data);
        ConsumerRecord record = (ConsumerRecord) data;
        OrderEntity order = (OrderEntity) record.value();
        if (order!=null) {
            if (order.getOrderUsername() != null) {
                if (calculateMoney(order) && tryReduceAmount(order)){
                    logger.info("ready order : [" + order.getId() + "]");
                    insertReadyOrder(order);
                }else {
                    logger.info("fail order : [" + order.getId() + "]-->" + order.getInfo());
                    insertReadyOrder(order);
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
            order.setOrderStatus(OrderEntity.OrderStatusEnum.CANCELED.getStatus())
                    .setInfo("库存不足");
            return false;
        }

        if (decrementAmountInRedis(order.getGoodId(), order.getAmount()) < 0){

            guavaCache.add(order.getGoodId());
            logger.debug("set good[" + order.getGoodId() + "] disabled");
            order.setOrderStatus(OrderEntity.OrderStatusEnum.CANCELED.getStatus())
                    .setInfo("库存不足");
            return false;
        }

        if (amountMapper.reduceAmount(order.getGoodId(), order.getAmount()) != 1){
            guavaCache.add(order.getGoodId());
            logger.debug("set good[" + order.getGoodId() + "] disabled");
            order.setOrderStatus(OrderEntity.OrderStatusEnum.CANCELED.getStatus())
                    .setInfo("库存不足");
            return false;
        }
        logger.debug("order[ " + order.getId() + "]-->good[" + order.getGoodId() + "] is enough");
        order.setOrderStatus(OrderEntity.OrderStatusEnum.READY.getStatus());
        return true;
    }

    private int decrementAmountInRedis(long goodId, int amount){
        Long res = (Long) redisTemplate.boundHashOps("amount").get(goodId);
        if (res == null) {
            return 0;
        }
        if (res > amount) {
            res = redisTemplate.boundHashOps("amount").increment(goodId, -amount);
            logger.debug("amount of good[" + goodId + "] is " + res + " after decrement");
            if (res == null) {
                return 0;
            }
            if (res < 0){
                incrementAmountInRedis(goodId, amount);
            }
            return Math.toIntExact(res);
        }
        return -1;
    }


    @Async
    public void incrementAmountInRedis(Long goodId, Integer amount){
        logger.debug("rollback amount of good[" + goodId + "] in redis");
        redisTemplate.boundHashOps("amount").increment(goodId, amount);
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
            order.setOrderStatus(OrderEntity.OrderStatusEnum.CANCELED.getStatus());
            order.setInfo("获取商品价格出错");
            return false;
        }
        logger.info("calculate order[" + order.getId() + "] money success");
        return true;
    }

    @Async
    public void insertReadyOrder(OrderEntity order){
        orderMapper.insertOrder(order);
    }
}
