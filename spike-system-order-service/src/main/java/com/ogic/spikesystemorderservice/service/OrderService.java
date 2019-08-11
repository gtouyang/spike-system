package com.ogic.spikesystemorderservice.service;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemapi.exception.ProductAmountLessThanZeroException;
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
public class OrderService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public final String DEFAULT_ORDER_HEAD = "ORDER";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 创建订单并存放在redis上
     *
     * @param productId 商品ID
     * @param userId    用户ID
     * @return 生成的订单
     */
    public OrderEntity createOrder(Long productId, Long userId, Integer amount) {
        Date current = new Date();
        OrderEntity order = new OrderEntity()
                .setProductId(productId)
                .setOrderUserId(userId)
                .setAmount(amount)
                .setOrderStatus(OrderEntity.OrderStatusEnum.READY)
                .setOrderTime(current);
        try {
            ProductEntity product = (ProductEntity) redisTemplate.boundHashOps("productMap").get(productId);

            assert product != null;
            if (product.getSpikeStartTime()==null||product.getSpikeEndTime()==null){
                order.setPayMoney(product.getOriginPrice());
            }

            if (current.after(product.getSpikeStartTime()) && current.before(product.getSpikeEndTime())) {
                order.setPayMoney(product.getSpikePrice());
            } else {
                order.setPayMoney(product.getOriginPrice());
            }
            StringBuilder builder = new StringBuilder();
            builder.append(DEFAULT_ORDER_HEAD)
                    .append(System.currentTimeMillis())
                    .append(new SecureRandom().longs());

            String orderId = builder.toString();
            order.setId(orderId);

            //不幸订单号重复
            while (! redisTemplate.boundValueOps(orderId).setIfAbsent(order)){
                builder = new StringBuilder();
                builder.append(DEFAULT_ORDER_HEAD)
                        .append(System.currentTimeMillis())
                        .append(new SecureRandom().longs());
                orderId = builder.toString();
                order.setId(orderId);
            }
        } catch (NullPointerException exception) {
            logger.error("can not find product[" + productId + "]");
            return null;
        }
        return order;
    }

    /**
     * 减少redis中的库存
     *
     * @param productId    商品ID
     * @param reduceAmount 减少的数量
     * @return 减少成功与否
     */
    public boolean reduceProductAmount(Long productId, Integer reduceAmount) throws ProductAmountLessThanZeroException {

        Optional result = Optional.ofNullable(redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.watch("amountOfProduct" + productId.toString());

                Long leaveAmount = (Long) redisOperations.boundValueOps("amountOfProduct" + productId.toString()).get();
                while (leaveAmount >= reduceAmount) {
                    redisOperations.multi();
                    redisOperations.boundValueOps("amountOfProduct" + productId.toString()).decrement(reduceAmount);
                    List<Long> resList = redisOperations.exec();
                    if (resList != null) {
                        return resList.get(0);
                    }
                    redisOperations.watch("amountOfProduct" + productId.toString());

                    leaveAmount = (Long) redisOperations.boundValueOps("amountOfProduct" + productId.toString()).get();

                }
                return null;
            }
        }));

        if (result.isPresent()) {
            Long leaveAmount = (Long) result.get();
            if (leaveAmount < 0){
                throw new ProductAmountLessThanZeroException(productId.toString());
            }
        }

        return false;
    }

    /**
     * 下单流程
     * @param productId 商品ID
     * @param userId    用户ID
     * @param amount    数量
     * @return  订单
     */
    public OrderEntity order(Long productId, Long userId, Integer amount){
        try {
            if (reduceProductAmount(productId, amount)) {
                OrderEntity order = createOrder(productId, userId, amount);
                if (order == null) {
                    redisTemplate.boundValueOps("amountOfProduct" + productId.toString()).increment(amount);
                }
                return order;
            }
        }catch (ProductAmountLessThanZeroException exception){
            logger.error(exception.getMessage());
        }
        return null;
    }
}
