package com.ogic.spikesystempayservice.mapper;

import com.ogic.spikesystemapi.entity.WalletEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author ogic
 */
@Mapper
public interface WalletMapper {

    /**
     * 根据用户名获得钱包实例
     *
     * @param username 用户名
     * @return 钱包实例
     */
    @Select("select * from wallet where username = #{username}")
    List<WalletEntity> getWalletByUsername(String username);


    /**
     * 根据钱包ID获得钱包实例
     *
     * @param id 钱包ID
     * @return 钱包实例
     */
    @Select("select * from wallet where id = #{id} limit 1")
    WalletEntity getWalletById(Long id);


    /**
     * 更新余额
     *
     * @param id      钱包ID
     * @param money   余额
     * @param version 版本
     * @return 更新成功与否
     */
    @Update("update wallet set money = #{money}, version = version+1 where id = #{id} and version = #{version}")
    Integer updateWalletMoney(Long id, Double money, Integer version);

    /**
     * 插入新钱包
     *
     * @param wallet 钱包实例
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into wallet(username, money, pay_password)" +
            "values(#{username}," +
            "#{money}," +
            "#{payPassword})")
    Integer insertWallet(WalletEntity wallet);
}
