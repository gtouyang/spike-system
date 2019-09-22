package com.ogic.spikesystemgoodservice.controller;

import com.ogic.spikesystemapi.entity.GoodEntity;
import com.ogic.spikesystemgoodservice.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@RestController
public class GoodController {

    @Autowired
    GoodService goodService;

    @GetMapping("/good")
    public Optional<GoodEntity> getGoodById(@RequestParam long id) {
        return Optional.ofNullable(goodService.getGoodById(id));
    }

    @PostMapping("/good")
    public Optional<Integer> addGood(@RequestBody GoodEntity good) {
        return Optional.of(goodService.addGood(good));
    }

    /**
     * 根据位移和数量获取商品列表
     *
     * @param offset
     * @param rows
     * @return
     */
    @GetMapping("/goods")
    public Optional<List<GoodEntity>> getGoods(@RequestParam long offset, @RequestParam int rows){
        return Optional.ofNullable(goodService.getGoods(offset, rows));
    }

    /**
     * 根据shopId获取商品列表
     *
     * @param shopId
     * @return
     */
    @GetMapping("/shop-goods")
    public Optional<List<GoodEntity>> getGoodsByShopId(@RequestParam long shopId){
        return Optional.ofNullable(goodService.getGoodsByShopId(shopId));
    }
}
