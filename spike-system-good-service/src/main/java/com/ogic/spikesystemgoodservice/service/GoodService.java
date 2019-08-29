package com.ogic.spikesystemgoodservice.service;

import com.ogic.spikesystemapi.entity.GoodEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
public interface GoodService {

    /**
     * 增加商品
     *
     * @param goodEntity
     * @return
     */
    Optional<Integer> addGood(GoodEntity goodEntity);

    /**
     * 根据商品ID获取商品
     *
     * @param id
     * @return
     */
    Optional<GoodEntity> getGoodById(Long id);

    /**
     * 根据位移和数量获取商品列表
     *
     * @param offset
     * @param rows
     * @return
     */
    Optional<List<GoodEntity>> getGoods(Long offset, Integer rows);
}
