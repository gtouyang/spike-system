package com.ogic.spikesystemorderqueryservice.controller;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemorderqueryservice.service.OrderQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@RestController
public class OrderController {

    @Autowired
    OrderQueryService orderQueryService;

    /**
     * 查询待支付订单
     * @param username
     * @return
     */
    @GetMapping("/readyOrders")
    public Optional<List<OrderEntity>> getReadyOrders(@RequestParam String username){
        return Optional.ofNullable(orderQueryService.getReadyOrders(username));
    }

    /**
     * 查询已完成订单
     * @param username
     * @return
     */
    @GetMapping("/finishedOrders")
    public Optional<List<OrderEntity>> getFinishedOrders(@RequestParam String username){
        return Optional.ofNullable(orderQueryService.getFinishedOrders(username));
    }

    /**
     * 查询所有订单
     * @param username
     * @return
     */
    @GetMapping("/allOrders")
    public Optional<List<OrderEntity>> getAllOrders(@RequestParam String username){
        return Optional.ofNullable(orderQueryService.getAllOrders(username));
    }

    /**
     * 根据ID获取订单
     * @param id
     * @return
     */
    @GetMapping("/order")
    public Optional<OrderEntity> getOrder(@RequestParam long id){
        return Optional.ofNullable(orderQueryService.getOrder(id));
    }

}
