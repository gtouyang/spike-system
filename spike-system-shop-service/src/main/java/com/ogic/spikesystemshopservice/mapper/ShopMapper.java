package com.ogic.spikesystemshopservice.mapper;

import com.ogic.spikesystemapi.entity.ShopEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author ogic
 */
@Mapper
public interface ShopMapper {

    /**
     * 根据商铺ID获得商铺
     *
     * @param id 商铺ID
     * @return
     */
    @Select("select * from shop where id = #{id} limit 1")
    ShopEntity getShopById(long id);

    /**
     * 根据卖家用户名获得商铺列表
     *
     * @param owner 商铺用户名
     * @return
     */
    @Select("select * from shop where owner = #{owner} order by id")
    List<ShopEntity> getShopsByOwner(String owner);

    /**
     * 插入新商铺
     *
     * @param shopEntity 商铺实例
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into shop(owner, shop_name, money)" +
            "values(" +
            "#{owner}, " +
            "#{shopName}, " +
            "#{money})")
    Integer insertShop(ShopEntity shopEntity);

    /**
     * 修改商铺余额
     *
     * @param shopEntity 商铺实例
     * @return
     */
    @Update("update table shop set money = #{money}, version = version+1 where id = #{id} and version = #{version}")
    Integer updateShopMoney(ShopEntity shopEntity);
}
