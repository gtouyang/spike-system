package com.ogic.spikesystemapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * 用户实体类
 * @author ogic
 * @date 2019-07-31
 */

@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    /**
     * 用户ID
     */
    @Id
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 上次修改时间
     */
    private Date updateTime;
}