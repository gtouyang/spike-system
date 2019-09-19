package com.ogic.spikesystemsyncservice.listener;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemsyncservice.mapper.OrderMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ogic
 */
@Component
public class SyncListener {

    @Resource
    OrderMapper orderMapper;

    @KafkaListener(id = "orderSyncListener", topics = {"readyOrder" ,"finishedOrder"})
    public void orderSync(Object data){
        OrderEntity order = (OrderEntity) data;
        OrderEntity exist = orderMapper.getOrderById(order.getId());
        if (exist == null){
            orderMapper.insertOrder(order);
        }else if (order.getOrderStatus().equals(OrderEntity.OrderStatusEnum.FINISHED)
                && exist.getOrderStatus().equals(OrderEntity.OrderStatusEnum.READY)){
            orderMapper.updateOrder(order);
        }
    }
}
