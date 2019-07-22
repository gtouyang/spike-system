package com.ogic.spikesystemorderservice.service;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    RedisTemplate redisTemplate;

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

        //如果redis中不存在该商品或获取最大订单号异常则不执行
        try {
            ProductEntity product = (ProductEntity) redisTemplate.boundHashOps("productMap").get(productId);

            if (product.getSpikeStartTime()==null||product.getSpikeEndTime()==null){
                order.setPayMoney(product.getOriginPrice());
            }

            if (current.after(product.getSpikeStartTime()) && current.before(product.getSpikeEndTime())) {
                order.setPayMoney(product.getSpikePrice());
            } else {
                order.setPayMoney(product.getOriginPrice());
            }
            Long orderId = (Long) redisTemplate.boundValueOps("maxOrderId").increment();

            order.setId(orderId);
            redisTemplate.boundHashOps("orderMap").put(orderId, order);
        } catch (NullPointerException exception) {
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
    public boolean reduceProductAmount(Long productId, Integer reduceAmount) {


        Long result = (Long) redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.watch("amountOfProduct" + productId.toString());

                Integer leaveAmount = (Integer) redisOperations.boundValueOps("amountOfProduct" + productId.toString()).get();
                while (leaveAmount >= reduceAmount) {
                    redisOperations.multi();
                    redisOperations.boundValueOps("amountOfProduct" + productId.toString()).decrement(reduceAmount);
                    List<Long> resList = redisOperations.exec();
                    if (resList != null) {
                        return resList.get(0);
                    }
                    redisOperations.watch("amountOfProduct" + productId.toString());

                    leaveAmount = (Integer) redisOperations.boundValueOps("amountOfProduct" + productId.toString()).get();

                }
                return null;
            }
        });

        if (result != null) {
            Long afterAmount = result;
            if (afterAmount >= 0) {
                return true;
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
    public OrderEntity buy(Long productId, Long userId, Integer amount) {
        if (reduceProductAmount(productId, amount)) {
            OrderEntity order = createOrder(productId, userId, amount);
            if (order==null){
                redisTemplate.boundValueOps("amountOfProduct" + productId.toString()).increment(amount);
            }
            return order;
        }
        return null;
    }
}
