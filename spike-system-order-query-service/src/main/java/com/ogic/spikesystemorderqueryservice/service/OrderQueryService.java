package com.ogic.spikesystemorderqueryservice.service;

import com.ogic.spikesystemapi.entity.OrderEntity;

import java.util.List;

/**
 * @author ogic
 */
public interface OrderQueryService {

    /**
     * 获取待支付订单
     * @param username
     * @return
     */
    List<OrderEntity> getReadyOrders(String username);

    /**
     * 获取已完成订单
     * @param username
     * @return
     */
    List<OrderEntity> getFinishedOrders(String username);

    /**
     * 获取所有订单
     * @param username
     * @return
     */
    List<OrderEntity> getAllOrders(String username);

    /**
     * 根据id获取订单
     * @param orderId
     * @return
     */
    OrderEntity getOrder(long orderId);

}
