package com.ogic.spikesystemproductservice.controller;

import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemproductservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ogic
 */
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/product/{id}")
    public ProductEntity getProductById(@PathVariable("id") long id) {
        ProductEntity product = productService.getProductById(id);
        return product;
    }
}
