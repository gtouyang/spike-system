package com.ogic.spikesystemapi.service;

import com.ogic.spikesystemapi.entity.GoodEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@FeignClient(value = "SPIKE-SYSTEM-GOOD-SERVICE")
public interface GoodExposeService {

    /**
     * 根据商品ID获取商品
     *
     * @param id 商品ID
     * @return 商品对象
     */
    @GetMapping("/good")
    Optional<GoodEntity> getGoodById(@RequestParam long id);

    /**
     * 添加商品
     *
     * @param good
     * @return
     */
    @PostMapping("/good")
    Optional<Integer> addGood(@RequestBody GoodEntity good);

    /**
     * 根据位移和数量获取商品列表
     *
     * @param offset
     * @param rows
     * @return
     */
    @GetMapping("/goods")
    Optional<List<GoodEntity>> getGoods(@RequestParam long offset, @RequestParam int rows);

    /**
     * 根据shopId获取商品列表
     *
     * @param shopId
     * @return
     */
    @GetMapping("/shop-goods")
    Optional<List<GoodEntity>> getGoodsByShopId(@RequestParam long shopId);
}
