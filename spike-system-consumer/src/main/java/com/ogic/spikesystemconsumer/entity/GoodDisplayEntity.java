package com.ogic.spikesystemconsumer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author ogic
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class GoodDisplayEntity {
    private String name;
    private String price;
    private String shop;
    private String seller;
    private String timeInfo;
    private String info;
}
