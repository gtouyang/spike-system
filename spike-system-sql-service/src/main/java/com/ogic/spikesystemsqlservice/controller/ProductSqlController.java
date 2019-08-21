package com.ogic.spikesystemsqlservice.controller;

import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemsqlservice.mapper.ProductMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@RestController
public class ProductSqlController {

    @Resource
    ProductMapper productMapper;

    @GetMapping(value = "/sql/select/product/{id}")
    public Optional<ProductEntity> getProductById(@PathVariable long id){
        return Optional.ofNullable(productMapper.getProductById(id));
    }

    @GetMapping(value = "/sql/select/products")
    public Optional<List<ProductEntity>> getProducts(@RequestParam long offest, @RequestParam Integer rows){
        return Optional.ofNullable(productMapper.getProducts(offest,rows));
    }

    @PostMapping(value = "/sql/insert/product")
    public Optional<Integer> insertProduct(@RequestParam ProductEntity product){
        return Optional.ofNullable(productMapper.insertProduct(product));
    }
}
