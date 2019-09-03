package com.ogic.spikesystemapi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * 订单对象实体类
 *
 * @author ogic
 * @date 2019-07-16
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class OrderEntity implements Serializable {

    /**
     * 订单状态枚举类型
     */
    public enum OrderStatusEnum {

        /**
         * 待支付
         */
        READY(Short.valueOf("0")),

        /**
         * 已完成
         */
        FINISHED(Short.valueOf("1")),

        /**
         * 已取消
         */
        CANCELED(Short.valueOf("-1"));

        private Short status;

        OrderStatusEnum(Short status) {
            this.status = status;
        }

        public Short getStatus() {
            return status;
        }
    }

    /**
     * 订单ID
     */
    private String id;

    /**
     * 下单时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;

    /**
     * 下单用户名
     */
    private String orderUsername;

    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatus;

    /**
     * 商品ID
     */
    private Long goodId;

    /**
     * 数量
     */
    private Integer amount;

    /**
     * 支付金额
     */
    private Double payMoney;

    /**
     * 支付时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**
     * 支付用户名
     */
    private String payUsername;

    private String info;
}
