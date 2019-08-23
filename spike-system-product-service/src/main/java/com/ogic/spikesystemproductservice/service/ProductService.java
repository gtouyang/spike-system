package com.ogic.spikesystemproductservice.service;

import com.ogic.spikesystemapi.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
public interface ProductService {

    /**
     * 增加商品
     * @param productEntity
     * @return
     */
    Optional<Integer> addProduct(ProductEntity productEntity);

    /**
     * 根据商品ID获取商品
     * @param id
     * @return
     */
    Optional<ProductEntity> getProductById(Long id);

    /**
     * 根据位移和数量获取商品列表
     * @param offset
     * @param rows
     * @return
     */
    Optional<List<ProductEntity>> getProducts(Long offset, Integer rows);
}
