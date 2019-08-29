package com.ogic.spikesystemorderservice.service;

import com.ogic.spikesystemapi.entity.OrderEntity;

/**
 * @author ogic
 */
public interface OrderService {

    /**
     * 下单
     * @param goodId
     * @param username
     * @param amount
     * @return
     */
    OrderEntity order(Long goodId, String username, Integer amount);
}
