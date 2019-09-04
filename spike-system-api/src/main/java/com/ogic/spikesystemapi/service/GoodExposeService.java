package com.ogic.spikesystemapi.service;

import com.ogic.spikesystemapi.entity.GoodEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

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
}
