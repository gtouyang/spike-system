package com.ogic.spikesystemorderqueryservice.service.impl;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemorderqueryservice.mapper.OrderMapper;
import com.ogic.spikesystemorderqueryservice.service.OrderQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ogic
 */
@Service
public class OrderQueryServiceImpl implements OrderQueryService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    OrderMapper orderMapper;

    @Override
    public List<OrderEntity> getReadyOrders(String username) {
        List<OrderEntity> orders = getAllOrders(username);
        if (orders != null && orders.size() > 0) {
            for (int i = 0; i < orders.size(); i++) {
                if (!orders.get(i).getOrderStatus().equals(OrderEntity.OrderStatusEnum.READY)) {
                    orders.remove(i);
                    i--;
                }else {
                    redisTemplate.boundValueOps(Long.toString(orders.get(i).getId())).setIfAbsent(orders.get(i), 15, TimeUnit.MINUTES);
                }
            }
        }
        return orders;
    }



    @Override
    public List<OrderEntity> getFinishedOrders(String username) {
        List<OrderEntity> orders = getAllOrders(username);
        if (orders != null && orders.size() > 0) {
            for (int i = 0; i < orders.size(); i++) {
                if (!orders.get(i).getOrderStatus().equals(OrderEntity.OrderStatusEnum.FINISHED)) {
                    orders.remove(i);
                    i--;
                }
            }
        }
        return orders;
    }

    @Override
    public List<OrderEntity> getAllOrders(String username) {
        return orderMapper.getOrders(username);
    }

    @Override
    public OrderEntity getOrder(long orderId) {
        OrderEntity order = (OrderEntity) redisTemplate.boundValueOps(Long.toString(orderId)).get();
        if (order == null){
            order = orderMapper.getOrder(orderId);
            if (order != null){
                redisTemplate.boundValueOps(Long.toString(orderId)).setIfAbsent(order, 15, TimeUnit.MINUTES);
            }
        }
        return order;
    }
}
