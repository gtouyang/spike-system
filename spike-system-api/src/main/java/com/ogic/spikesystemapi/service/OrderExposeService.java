package com.ogic.spikesystemapi.service;

import com.ogic.spikesystemapi.entity.OrderEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ogic
 */
@FeignClient(value = "SPIKE-SYSTEM-ORDER-SERVICE")
public interface OrderExposeService {

    /**
     * 下单
     * @param order 订单对象
     * @return  订单对象
     */
    @PostMapping("/order")
    OrderEntity order(@RequestParam("order") OrderEntity order);
}
