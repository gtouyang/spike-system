package com.ogic.spikesystemgoodservice.service.impl;

import com.ogic.spikesystemapi.entity.GoodEntity;
import com.ogic.spikesystemgoodservice.mapper.GoodMapper;
import com.ogic.spikesystemgoodservice.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 商品相关服务,对商品进行的所有操作都通过这个service来提供服务
 *
 * @author ogic
 * @date 2019-07-16
 */
@Service
public class GoodServiceImpl implements GoodService {

    private final String GOOD_AMOUNT_HEADER = "amountOfGood";
    @Resource
    GoodMapper goodMapper;
    /**
     * 自动注入redis模板，使用该模板操作redis
     */
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 自定义的形式添加一个商品,要注意good内的name,amount,originPrice属性不应为空,并且商品的ID和创建时间由MySQL决定,定义没用
     *
     * @param goodEntity 商品对象
     */
    @Override
    public Optional<Integer> addGood(GoodEntity goodEntity) {
        //这一条命令可有可无
        goodEntity.setId(null).setCreateTime(null);

        return Optional.ofNullable(goodMapper.insertGood(goodEntity));
    }

    /**
     * 根据ID从MySQL中获取指定的商品
     *
     * @param id 商品ID
     * @return 商品对象
     */
    @Override
    public Optional<GoodEntity> getGoodById(Long id) {

        GoodEntity cache = (GoodEntity) redisTemplate.boundHashOps("goodMap").get(id);
        if (cache == null) {
            Optional<GoodEntity> goodOptional = Optional.ofNullable(goodMapper.getGoodById(id));
            if (goodOptional.isPresent()) {
                redisTemplate.boundHashOps("goodMap").put(id, goodOptional.get());
                redisTemplate.boundZSetOps("goodZSet").add(id, 1);

                if (redisTemplate.boundValueOps(GOOD_AMOUNT_HEADER + id).get() == null) {
                    redisTemplate.boundValueOps(GOOD_AMOUNT_HEADER + id).set(goodOptional.get().getAmount());
                }
            }
            return goodOptional;
        } else {
            redisTemplate.boundZSetOps("goodZSet").incrementScore(id, 1);
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
    public Optional<List<GoodEntity>> getGoods(Long offset, Integer rows) {
        Optional<List<GoodEntity>> goods = Optional.ofNullable(goodMapper.getGoods(offset, rows));
        goods.ifPresent(this::updateRedis);
        return goods;
    }

    /**
     * 将列表中的商品添加到redis中并在热度加一
     *
     * @param goods 商品
     */
    @Async
    protected void updateRedis(List<GoodEntity> goods) {
        for (GoodEntity good : goods) {
            GoodEntity cache = (GoodEntity) redisTemplate.boundHashOps("goodMap").get(good.getId());
            if (cache == null) {
                redisTemplate.boundHashOps("goodMap").put(good.getId(), good);
                redisTemplate.boundZSetOps("goodZSet").add(good.getId(), 1);
            } else {
                redisTemplate.boundZSetOps("goodZSet").incrementScore(good.getId(), 1);
            }
        }
    }

}
