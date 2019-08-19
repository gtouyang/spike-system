package com.ogic.spikesystemsqlservice.controller;

import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemsqlservice.annotation.Master;
import com.ogic.spikesystemsqlservice.annotation.Slave;
import com.ogic.spikesystemsqlservice.mapper.ProductMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@Controller
public class ProductSqlController {

    @Resource
    ProductMapper productMapper;

    @Slave
    @GetMapping(value = "/sql/select/product/{id}")
    public Optional<ProductEntity> getProductById(@PathVariable Long id){
        return Optional.ofNullable(productMapper.getProductById(id));
    }

    @Slave
    @GetMapping(value = "/sql/select/products")
    public Optional<List<ProductEntity>> getProducts(@RequestParam Long offest, @RequestParam Integer rows){
        return Optional.ofNullable(productMapper.getProducts(offest,rows));
    }

    @Master
    @PutMapping(value = "sql/insert/product")
    public Optional<Integer> insertProduct(@RequestParam ProductEntity product){
        return Optional.ofNullable(productMapper.insertProduct(product));
    }
}
