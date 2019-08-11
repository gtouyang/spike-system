package com.ogic.spikesystemapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ogic
 */
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletEntity implements Serializable {

    /**
     * 钱包ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 余额
     */
    private Double money;

    /**
     * 支付密码
     */
    private String payPassword;

    /**
     * 版本,用于乐观锁
     */
    private Integer version;
}
