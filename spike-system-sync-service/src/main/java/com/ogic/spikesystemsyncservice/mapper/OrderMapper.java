package com.ogic.spikesystemsyncservice.mapper;

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
    @Select("select * from order where id = #{id}")
    OrderEntity getOrderById(long id);

    /**
     * 插入新订单
     * @param order
     * @return
     */
    @Insert("insert into order(id, order_time, order_username, order_status, good_id, amount, pay_money, pay_time, pay_wallet_id, info)" +
            "values(" +
            "#{id}," +
            "#{orderTime}," +
            "#{orderUsername}," +
            "#{orderStatus}," +
            "#{goodId}," +
            "#{amount}," +
            "#{payMoney}," +
            "#{payTime}," +
            "#{payWalletId}," +
            "#{info})")
    int insertOrder(OrderEntity order);

    /**
     * 更新订单信息
     * @param order
     * @return
     */
    @Update("update order set" +
            "order_status = #{orderStatus}" +
            "pay_time = #{payTime}" +
            "pay_wallet_id = #{payWalletId}" +
            "info = #{info}" +
            "where id = #{id}")
    int updateOrder(OrderEntity order);
}
