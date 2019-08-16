package com.ogic.spikesystemapi.service;

import com.ogic.spikesystemapi.entity.ProductEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * @author ogic
 */
@FeignClient(value = "SPIKE-SYSTEM-PRODUCT-SERVICE")
public interface ProductExposeService {

    /**
     * 根据商品ID获取商品
     * @param id    商品ID
     * @return      商品对象
     */
    @GetMapping("/product/{id}")
    Optional<ProductEntity> getProductById(@PathVariable("id") long id);
}
