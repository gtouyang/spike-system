package com.ogic.spikesystemapi.service;

import com.ogic.spikesystemapi.entity.OrderEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * @author ogic
 */
@FeignClient(value = "SPIKE-SYSTEM-ORDER-SERVICE")
public interface OrderExposeService {

    /**
     * 下单
     * @param goodId 商品ID
     * @param amount 数量
     * @param token 令牌
     * @return  订单对象
     */
    @PostMapping(value = "/order")
    Optional<OrderEntity> order(@RequestParam Long goodId, @RequestParam Integer amount, @RequestHeader(HttpHeaders.AUTHORIZATION) String token);
}
