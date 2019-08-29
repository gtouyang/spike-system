package com.ogic.spikesystemapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ogic
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ShopEntity implements Serializable {

    /**
     * 店铺ID
     */
    public Long id;

    /**
     * 卖家用户名
     */
    public String owner;

    /**
     * 店铺名
     */
    public String shopName;

    /**
     * 店铺余额
     */
    public Double money;
}
