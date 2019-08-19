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
public class ProductDisplayEntity {
    private String name;
    private String price;
    private String timeInfo;
    private String info;
}
