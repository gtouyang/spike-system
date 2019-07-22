package com.ogic.spikesystemorderservice.controller;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemorderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ogic
 */
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/buy")
    public OrderEntity buy(OrderEntity order){
        return orderService.buy(order.getProductId(), order.getOrderUserId(), order.getAmount());
    }

}
