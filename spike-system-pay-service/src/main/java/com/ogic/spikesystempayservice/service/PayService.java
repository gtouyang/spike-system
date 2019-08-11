package com.ogic.spikesystempayservice.service;

import com.ogic.spikesystemapi.entity.WalletEntity;

import java.util.List;

public interface PayService {

    /**
     * 获得该用户的所有钱包
     * @param username  用户名
     * @return  钱包列表
     */
    List<WalletEntity> getUserAllWallets(String username);

    /**
     * 获得可以支付的钱包列表
     * @param username  用户名
     * @param orderId   订单ID
     * @return  钱包列表
     */
    List<WalletEntity> getUserAllWallets(String username, String orderId);

    /**
     * 使用钱包给订单支付
     * @param orderId   订单ID
     * @param walletID  钱包
     * @param pasPassword   支付密码
     * @return  支付成功与否
     */
    Boolean payOrderByWallet(Long walletID, String pasPassword, String orderId);
}
