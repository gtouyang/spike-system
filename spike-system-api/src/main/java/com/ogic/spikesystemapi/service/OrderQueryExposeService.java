package com.ogic.spikesystemapi.service;

import com.ogic.spikesystemapi.entity.OrderEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@FeignClient(value = "SPIKE-SYSTEM-ORDER-QUERY-SERVICE")
public interface OrderQueryExposeService {

    /**
     * 查询待支付订单
     * @param username
     * @return
     */
    @GetMapping("/readyOrders")
    Optional<List<OrderEntity>> getReadyOrders(@RequestParam String username);

    /**
     * 查询已完成订单
     * @param username
     * @return
     */
    @GetMapping("/finishedOrders")
    Optional<List<OrderEntity>> getFinishedOrders(@RequestParam String username);

    /**
     * 查询所有订单
     * @param username
     * @return
     */
    @GetMapping("/allOrders")
    Optional<List<OrderEntity>> getAllOrders(@RequestParam String username);
}
