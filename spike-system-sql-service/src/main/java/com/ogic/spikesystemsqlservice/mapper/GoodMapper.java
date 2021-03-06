package com.ogic.spikesystemsqlservice.mapper;

import com.ogic.spikesystemapi.entity.GoodEntity;
import com.ogic.spikesystemsqlservice.annotation.Master;
import com.ogic.spikesystemsqlservice.annotation.Slave;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 商品的MyBatis接口,所有与MySQL相关的操作都通过它来实现
 *
 * @author ogic
 * @date 2019-07-16
 */
@Mapper
public interface GoodMapper {

    /**
     * 根据ID获取商品
     *
     * @param id
     * @return
     */
    @Slave
    @Select("select * from good where id = #{id} join amount")
    GoodEntity getGoodById(Long id);

    /**
     * 获取商品列表
     *
     * @param offset
     * @param rows
     * @return
     */
    @Slave
    @Select("select * from good order by id desc limit #{offset}, #{rows}")
    List<GoodEntity> getGoods(Long offset, Integer rows);

    /**
     * 插入新商品
     *
     * @param good
     * @return
     */
    @Master
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into good(name, amount, origin_price, spike_price, spike_start_time, spike_end_time, image_url, info, shop_id)" +
            "values(#{name}, " +
            "#{amount}, " +
            "#{originPrice}, " +
            "#{spikePrice}, " +
            "#{spikeStartTime}, " +
            "#{spikeEndTime}, " +
            "#{imageUrl}, " +
            "#{info}," +
            "#{shopId})")
    Integer insertGood(GoodEntity good);

    /**
     * 更新库存
     *
     * @param id
     * @param amount
     * @param version
     * @return
     */
    @Master
    @Update("update amount set amount = #{amount}, version = version + 1 where id = #{id} and version = #{version}")
    Integer updateGoodAmount(Long id, Integer amount, Integer version);
}
