package com.ogic.spikesystemorderservice.service.impl;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.entity.GoodEntity;
import com.ogic.spikesystemapi.exception.GoodAmountLessThanZeroException;
import com.ogic.spikesystemorderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@Service
public class OrderServiceImpl implements OrderService {

    public final String DEFAULT_ORDER_HEAD = "ORDER";
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 创建订单并存放在redis上
     *
     * @param goodId   商品ID
     * @param username 用户名
     * @return 生成的订单
     */
    private OrderEntity createOrder(Long goodId, String username, Integer amount) {
        Date current = new Date();
        OrderEntity order = new OrderEntity()
                .setGoodId(goodId)
                .setOrderUsername(username)
                .setAmount(amount)
                .setOrderStatus(OrderEntity.OrderStatusEnum.READY)
                .setOrderTime(current);
        GoodEntity good = (GoodEntity) redisTemplate.boundHashOps("goodMap").get(goodId);

        if (good == null || good.getOriginPrice() == null) {
            return null;
        }

        if (good.getSpikeStartTime() == null || good.getSpikeEndTime() == null) {
            order.setPayMoney(good.getOriginPrice());
        } else if (current.after(good.getSpikeStartTime()) && current.before(good.getSpikeEndTime())) {
            order.setPayMoney(good.getSpikePrice());
        } else {
            order.setPayMoney(good.getOriginPrice());
        }
        StringBuilder builder = new StringBuilder();
        builder.append(DEFAULT_ORDER_HEAD)
                .append(System.currentTimeMillis())
                .append(randomNumbers(10));

        String orderId = builder.toString();
        order.setId(orderId);

        //不幸订单号重复
        while (!redisTemplate.boundValueOps(orderId).setIfAbsent(order)) {
            builder = new StringBuilder();
            builder.append(DEFAULT_ORDER_HEAD)
                    .append(System.currentTimeMillis())
                    .append(new SecureRandom().longs());
            orderId = builder.toString();
            order.setId(orderId);
        }

        return order;
    }

    /**
     * 减少redis中的库存
     *
     * @param goodId       商品ID
     * @param reduceAmount 减少的数量
     * @return 减少成功与否
     */
    private boolean reduceGoodAmount(Long goodId, Integer reduceAmount) throws GoodAmountLessThanZeroException {

        Optional result = Optional.ofNullable(redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.watch("amountOfGood" + goodId.toString());

                Integer leaveAmount = (Integer) redisOperations.boundValueOps("amountOfGood" + goodId.toString()).get();
                while (leaveAmount >= reduceAmount) {
                    redisOperations.multi();
                    redisOperations.boundValueOps("amountOfGood" + goodId.toString()).decrement(reduceAmount);
                    List<Integer> resList = redisOperations.exec();
                    if (resList != null) {
                        return resList.get(0);
                    }
                    redisOperations.watch("amountOfGood" + goodId.toString());

                    leaveAmount = (Integer) redisOperations.boundValueOps("amountOfGood" + goodId.toString()).get();

                }
                return null;
            }
        }));

        if (result.isPresent()) {
            Integer leaveAmount = Integer.parseInt(result.get().toString());
            if (leaveAmount < 0) {
                throw new GoodAmountLessThanZeroException(goodId.toString());
            } else {
                return true;
            }
        }

        return false;
    }

    /**
     * 下单流程
     *
     * @param goodId   商品ID
     * @param username 用户名
     * @param amount   数量
     * @return 订单
     */
    @Override
    public OrderEntity order(Long goodId, String username, Integer amount) {
        try {
            if (reduceGoodAmount(goodId, amount)) {
                OrderEntity order = createOrder(goodId, username, amount);
                if (order == null) {
                    redisTemplate.boundValueOps("amountOfGood" + goodId.toString()).increment(amount);
                }
                return order;
            }
        } catch (GoodAmountLessThanZeroException exception) {
            logger.error(exception.getMessage());
        }
        return null;
    }

    private String randomNumbers(int amount) {
        StringBuilder builder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < amount; i++) {
            builder.append(random.nextInt(9));
        }
        return builder.toString();
    }
}
