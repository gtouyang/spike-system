package com.ogic.spikesystempayservice.service;

import com.ogic.spikesystemapi.entity.WalletEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
public interface PayService {

    /**
     * 获得该用户的所有钱包
     *
     * @param username 用户名
     * @return 钱包列表
     */
    Optional<List<WalletEntity>> getUserAllWallets(String username);

    /**
     * 获得可以支付的钱包列表
     *
     * @param username 用户名
     * @param orderId  订单ID
     * @return 钱包列表
     */
    Optional<List<WalletEntity>> getUserAllWallets(String username, long orderId);

    /**
     * 使用钱包给订单支付
     *
     * @param orderId     订单ID
     * @param walletId    钱包
     * @param payPassword 支付密码
     * @return 支付成功与否
     */
    Optional<Boolean> payOrderByWallet(long walletId, String payPassword, long orderId);

    /**
     * 添加钱包
     *
     * @param wallet
     * @return
     */
    Optional<Integer> addWallet(WalletEntity wallet);
}