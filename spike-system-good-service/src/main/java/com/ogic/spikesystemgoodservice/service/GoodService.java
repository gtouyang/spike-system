package com.ogic.spikesystemgoodservice.service;

import com.ogic.spikesystemapi.entity.GoodEntity;

import java.util.List;

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
    int addGood(GoodEntity goodEntity);

    /**
     * 根据商品ID获取商品
     *
     * @param id
     * @return
     */
    GoodEntity getGoodById(long id);

    /**
     * 根据位移和数量获取商品列表
     *
     * @param offset
     * @param rows
     * @return
     */
    List<GoodEntity> getGoods(long offset, int rows);
}
