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

    @PostMapping("/order")
    public OrderEntity order(@RequestParam OrderEntity order){
        return orderService.order(order.getProductId(), order.getOrderUserId(), order.getAmount());
    }

}
