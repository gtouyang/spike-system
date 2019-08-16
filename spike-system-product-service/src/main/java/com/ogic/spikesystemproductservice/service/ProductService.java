package com.ogic.spikesystemproductservice.service;

import com.ogic.spikesystemapi.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
public interface ProductService {

    void addProduct(ProductEntity productEntity);

    Optional<ProductEntity> getProductById(Long id);

    Optional<List<ProductEntity>> getProducts(Long offset, Integer rows);
}
