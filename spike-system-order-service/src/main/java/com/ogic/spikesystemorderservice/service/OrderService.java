package com.ogic.spikesystemorderservice.service;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    RedisTemplate redisTemplate;

    public OrderEntity createOrder(Long productId, Long userId){
        Date current = new Date();
        OrderEntity order = new OrderEntity()
                .setProductId(productId)
                .setOrderUserId(userId)
                .setOrderStatus(OrderEntity.OrderStatusEnum.READY)
                .setOrderTime(current);
        ProductEntity product = (ProductEntity) redisTemplate.boundHashOps("productMap").get(productId);
        if (current.after(product.getSpikeStartTime()) && current.before(product.getSpikeEndTime())){
            order.setPayMoney(product.getSpikePrice());
        }else {
            order.setPayMoney(product.getOriginPrice());
        }
        Long orderId = (Long) redisTemplate.boundValueOps("maxOrderId").increment();
        order.setId(orderId);
        redisTemplate.boundHashOps("orderMap").put(orderId, order);
        return order;
    }

    public boolean reduceProductAmount(Long productId, Integer reduceAmount){


        redisTemplate.watch("amountOfProduct" + productId);
        Integer leaveAmount = (Integer) redisTemplate.boundValueOps("amountOfProduct"+productId).get();
        if (leaveAmount!=null && leaveAmount.compareTo(reduceAmount) >= 0){
            redisTemplate.multi();
            redisTemplate.boundValueOps("amountOfProduct" + productId).decrement(reduceAmount);
            List<String> resList = redisTemplate.exec();
            Integer afterAmount = Integer.parseInt(resList.get(0));
            if (afterAmount >= 0){
                return true;
            }
        }

        return false;
    }

    public OrderEntity buy(Long productId, Long userId, Integer amount){
        if (reduceProductAmount(productId, amount)){
            return createOrder(productId,userId);
        }
        return null;
    }
}
