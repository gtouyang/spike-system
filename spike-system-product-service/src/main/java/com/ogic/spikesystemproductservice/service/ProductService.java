package com.ogic.spikesystemproductservice.service;

import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemproductservice.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 商品相关服务,对商品进行的所有操作都通过这个service来提供服务
 *
 * @author ogic
 * @date 2019-07-16
 */
@Service
public class ProductService {

    /**
     * 自动注入mapper用于操作数据库,标红线不影响运行,运行时会自动注入bean
     */
    @Autowired
    ProductMapper productMapper;

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
    public void addProduct(ProductEntity productEntity) {
        //这一条命令可有可无
        productEntity.setId(null).setCreateTime(null);

        //最后使用ProductMapper来插入到数据库中
        productMapper.insertProduct(productEntity);
    }

    /**
     * 添加一个商品所需的必要信息
     *
     * @param productName 商品名
     * @param amount      库存
     * @param originPrice 商品原价
     */
    public void addProduct(String productName, Integer amount, Double originPrice) {
        ProductEntity product = new ProductEntity();
        product.setName(productName);
        product.setAmount(amount);
        product.setOriginPrice(originPrice);
        addProduct(product);
    }

    /**
     * 根据ID从MySQL中获取指定的商品
     * @param id 商品ID
     * @return 商品对象
     */
    public ProductEntity getProductById(Long id) {

        ProductEntity cache = (ProductEntity)  redisTemplate.boundHashOps("productMap").get(id);
        if (cache == null) {
            ProductEntity product = productMapper.getProductById(id);
            redisTemplate.boundHashOps("productMap").put(id,product);
            redisTemplate.boundZSetOps("productZSet").add(id, 1);

            if (redisTemplate.boundValueOps("amountOfProduct"+id).get() == null){
                redisTemplate.boundValueOps("amountOfProduct"+id).set(product.getAmount());
            }

            return product;
        } else {
            redisTemplate.boundZSetOps("productZSet").incrementScore(id, 1);
            return cache;
        }
    }


    /**
     * 获取指定数量和位置的商品
     *
     * @param offset 偏移量
     * @param rows   行数
     * @return 商品列表
     */
    public List<ProductEntity> getProducts(Integer offset, Integer rows) {
        List<ProductEntity> products = productMapper.getProducts(offset, rows);
        updateRedis(products);
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

    /**
     * 获取MySQL中所有的商品,不建议使用该方法,因为它获取的是所有
     *
     * @return MySQL所有的商品
     */
    @Deprecated
    public List<ProductEntity> getAllProduct() {
        return productMapper.getAllProductsOrderByCreateTime();
    }

}
