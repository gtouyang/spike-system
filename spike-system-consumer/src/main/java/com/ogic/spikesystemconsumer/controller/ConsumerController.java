package com.ogic.spikesystemconsumer.controller;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author ogic
 */
@RestController
public class ConsumerController {
    private static final String PRODUCT_SERVICE_REST_URL_PREFIX = "http://SPIKE-SYSTEM-PRODUCT-SERVICE";

    private static final String ORDER_SERVICE_REST_URL_PREFIX = "http://SPIKE-SYSTEM-ORDER-SERVICE";

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/product/{id}")
    public ProductEntity getProductById(@PathVariable("id") long id) {
        return restTemplate.getForObject(PRODUCT_SERVICE_REST_URL_PREFIX+"/product/"+id, ProductEntity.class);
    }

    @PostMapping("/buy")
    public OrderEntity buy(OrderEntity order){
        return restTemplate.postForObject(ORDER_SERVICE_REST_URL_PREFIX + "/buy", order, OrderEntity.class);
    }
}
