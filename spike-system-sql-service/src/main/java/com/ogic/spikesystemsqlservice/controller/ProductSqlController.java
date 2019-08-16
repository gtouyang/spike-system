package com.ogic.spikesystemsqlservice.controller;

import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemsqlservice.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@Controller
public class ProductSqlController {

    @Autowired
    ProductMapper productMapper;

    @GetMapping(value = "/sql/select/product/{id}")
    public Optional<ProductEntity> getProductById(@PathVariable Long id){
        return Optional.ofNullable(productMapper.getProductById(id));
    }

    @GetMapping(value = "/sql/select/products")
    public Optional<List<ProductEntity>> getProducts(@RequestParam Long offest, @RequestParam Integer rows){
        return Optional.ofNullable(productMapper.getProducts(offest,rows));
    }

    @PutMapping(value = "sql/insert/product")
    public Optional<Integer> insertProduct(@RequestParam ProductEntity product){
        return Optional.ofNullable(productMapper.insertProduct(product));
    }
}
