package com.ogic.spikesystemauthenticationservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

@AllArgsConstructor
public class JwtUser implements UserDetails {

    /**
     * 用户ID
     */
    private final String id;

    /**
     * 用户名
     */
    private final String username;

    /**
     * 密码
     */
    private final String password;

    /**
     * 用户邮箱
     */
    private final String email;

    /**
     * 用户角色
     */
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * 上次密码修改时间
     */
    private final Date lastPasswordResetDate;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    public String getId(){
        return id;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }


    /**
     * @return 账户是否未过期
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return 账户是否未锁定
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return 密码是否未过期
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return  账户是否激活
     */
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
