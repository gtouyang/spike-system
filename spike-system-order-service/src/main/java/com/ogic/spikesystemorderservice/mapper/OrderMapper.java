package com.ogic.spikesystemorderservice.mapper;

import com.ogic.spikesystemapi.entity.OrderEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author ogic
 */
@Mapper
public interface OrderMapper {

    /**
     * 根据id获取订单
     * @param id
     * @return
     */
    @Select("select * from indent where id = #{id}")
    OrderEntity getOrderById(long id);

    /**
     * 插入新订单
     * @param order
     * @return
     */
    @Insert("insert into indent(id, order_time, order_username, order_status, good_id, amount, pay_money, info)" +
            "values(" +
            "#{id}," +
            "#{orderTime}," +
            "#{orderUsername}," +
            "#{orderStatus}," +
            "#{goodId}," +
            "#{amount}," +
            "#{payMoney}," +
            "#{info})")
    int insertOrder(OrderEntity order);
}
