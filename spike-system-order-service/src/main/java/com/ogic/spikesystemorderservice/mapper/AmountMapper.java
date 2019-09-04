package com.ogic.spikesystemorderservice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author ogic
 */
@Mapper
public interface AmountMapper {

    /**
     * 减少库存
     * @param goodId
     * @param reduce
     * @return
     */
    @Update("update amount set amount = amount - #{reduce} where id = #{goodId} and amount >= reduce")
    Integer reduceAmount(Long goodId, Integer reduce);

    /**
     * 减少库存
     * @param goodId
     * @param add
     * @return
     */
    @Update("update amount set amount = amount + #{add} where id = #{goodId}")
    Integer addAmount(Long goodId, Integer add);
}
