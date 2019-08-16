package com.ogic.spikesystemapi.service;

import com.ogic.spikesystemapi.entity.ProductEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@FeignClient(value = "SPIKE-SYSTEM-SQL-SERVICE")
public interface ProductSqlExposeService {

    /**
     * 根据ID查询商品
     * @param id
     * @return
     */
    @GetMapping(value = "/sql/select/product/{id}")
    Optional<ProductEntity> getProductById(@PathVariable Long id);

    /**
     * 根据在数据库的位置查询商品
     * @param offest
     * @param rows
     * @return
     */
    @GetMapping(value = "/sql/select/products")
    Optional<List<ProductEntity>> getProducts(@RequestParam Long offest, @RequestParam Integer rows);

    /**
     * 插入新商品
     * @param product
     */
    @PutMapping(value = "sql/insert/product")
    Optional<Integer> insertProduct(@RequestParam ProductEntity product);
}
