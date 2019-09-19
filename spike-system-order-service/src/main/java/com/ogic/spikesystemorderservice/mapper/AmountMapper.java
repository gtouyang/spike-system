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
    @Update("update good set amount = amount - #{reduce} where id = #{goodId} and amount >= #{reduce}")
    Integer reduceAmount(long goodId, int reduce);
}
