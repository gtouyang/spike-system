package com.ogic.spikesystemorderqueryservice.mapper;

import com.ogic.spikesystemapi.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ogic
 */
@Mapper
public interface OrderMapper {

    /**
     * 获取订单列表
     * @param username
     * @return
     */
    @Select("select * from indent where order_username = #{username}")
    List<OrderEntity> getOrders(String username);

    /**
     * 根据id获取订单
     * @param orderId
     * @return
     */
    @Select(("select * from indent where id = #{orderId}"))
    OrderEntity getOrder(long orderId);
}
