package com.ogic.spikesystemproductservice.service.impl;

import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemapi.service.SqlExposeService;
import com.ogic.spikesystemproductservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * 商品相关服务,对商品进行的所有操作都通过这个service来提供服务
 *
 * @author ogic
 * @date 2019-07-16
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    SqlExposeService sqlExposeService;

    /**
     * 自动注入redis模板，使用该模板操作redis
     */
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 自定义的形式添加一个商品,要注意product内的name,amount,originPrice属性不应为空,并且商品的ID和创建时间由MySQL决定,定义没用
     *
     * @param productEntity 商品对象
     */
    @Override
    public void addProduct(ProductEntity productEntity) {
        //这一条命令可有可无
        productEntity.setId(null).setCreateTime(null);

        sqlExposeService.insertProduct(productEntity);
    }

    /**
     * 根据ID从MySQL中获取指定的商品
     * @param id 商品ID
     * @return 商品对象
     */
    @Override
    public Optional<ProductEntity> getProductById(Long id) {

        ProductEntity cache = (ProductEntity)  redisTemplate.boundHashOps("productMap").get(id);
        if (cache == null) {
            Optional<ProductEntity> productOptional = sqlExposeService.getProductById(id);
            if (productOptional.isPresent()) {
                redisTemplate.boundHashOps("productMap").put(id, productOptional.get());
                redisTemplate.boundZSetOps("productZSet").add(id, 1);

                if (redisTemplate.boundValueOps("amountOfProduct" + id).get() == null) {
                    redisTemplate.boundValueOps("amountOfProduct" + id).set(productOptional.get().getAmount());
                }
            }
            return productOptional;
        } else {
            redisTemplate.boundZSetOps("productZSet").incrementScore(id, 1);
            return Optional.of(cache);
        }
    }


    /**
     * 获取指定数量和位置的商品
     *
     * @param offset 偏移量
     * @param rows   行数
     * @return 商品列表
     */
    @Override
    public Optional<List<ProductEntity>> getProducts(Long offset, Integer rows) {
        Optional<List<ProductEntity>> products = sqlExposeService.getProducts(offset, rows);
        products.ifPresent(this::updateRedis);
        return products;
    }

    /**
     * 将列表中的商品添加到redis中并在热度加一
     *
     * @param products 商品
     */
    @Async
    protected void updateRedis(List<ProductEntity> products) {
        for (ProductEntity product : products) {
            ProductEntity cache = (ProductEntity) redisTemplate.boundHashOps("productMap").get(product.getId());
            if (cache == null) {
                redisTemplate.boundHashOps("productMap").put(product.getId(),product);
                redisTemplate.boundZSetOps("productZSet").add(product.getId(), 1);
            } else {
                redisTemplate.boundZSetOps("productZSet").incrementScore(product.getId(), 1);
            }
        }
    }

}
