package com.ogic.spikesystemproductservice.controller;

import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemproductservice.service.ProductService;
import com.ogic.spikesystemproductservice.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author ogic
 */
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/product/{id}")
    public Optional<ProductEntity> getProductById(@PathVariable("id") long id) {
        return productService.getProductById(id);
    }

    @PostMapping("/product")
    public Optional<Integer> addProduct(@RequestParam ProductEntity product) {
        return productService.addProduct(product);
    }
}
