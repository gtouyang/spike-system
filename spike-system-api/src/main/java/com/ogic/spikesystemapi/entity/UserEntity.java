package com.ogic.spikesystemapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户实体类
 *
 * @author ogic
 * @date 2019-07-17
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class UserEntity {

    /**
     * 默认哈希类型
     */
    public static final String DEFAULT_HASH_TYPE = "MD5";

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户帐号
     */
    private String accountNumber;

    /**
     * 用户密码
     * 注意不要明码存,存哈希值
     */
    private String password;

    /**
     * 密码哈希用的盐
     */
    private String salt;

    /**
     * 哈希类型
     */
    private String hashType = DEFAULT_HASH_TYPE;

    /**
     * 用户余额
     */
    private Double money;
}
