package com.ogic.spikesystemapi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 订单对象实体类
 *
 * @author ogic
 * @date 2019-07-16
 */
@AllArgsConstructor
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
        READY((short) 0),

        /**
         * 已完成
         */
        FINISHED((short) 1),

        /**
         * 已取消
         */
        CANCELED((short) -1),

        /**
         * 正在下单
         */
        ORDERING((short) 10);

        private short status;

        OrderStatusEnum(short status) {
            this.status = status;
        }

        public short getStatus() {
            return status;
        }
    }

    /**
     * 订单ID
     */
    private long id;

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
    private short orderStatus;

    /**
     * 商品ID
     */
    private long goodId;

    /**
     * 数量
     */
    private int amount;

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
     * 支付用钱包
     */
    private Long payWalletId;

    /**
     * 备注
     */
    private String info;

    public OrderEntity(){
        orderTime = new Date();
        id = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(orderTime) + randomNumbers(2));
        orderStatus = OrderStatusEnum.ORDERING.getStatus();
    }

    private String randomNumbers(int size) {
        StringBuilder builder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < size; i++) {
            builder.append(random.nextInt(9));
        }
        return builder.toString();
    }
}
