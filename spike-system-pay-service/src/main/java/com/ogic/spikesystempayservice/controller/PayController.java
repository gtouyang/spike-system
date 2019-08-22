package com.ogic.spikesystempayservice.controller;

import com.ogic.spikesystemapi.entity.WalletEntity;
import com.ogic.spikesystempayservice.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@RestController
public class PayController {

    @Autowired
    PayService payService;

    /**
     * 获得该用户的所有钱包
     *
     * @param username 用户名
     * @return 钱包列表
     */
    @GetMapping("/wallets/{username}")
    public Optional<List<WalletEntity>> getUserAllWallets(@PathVariable String username) {
        return payService.getUserAllWallets(username);
    }

    /**
     * 获得可以支付的钱包列表
     *
     * @param username 用户名
     * @param orderId  订单ID
     * @return 钱包列表
     */
    @GetMapping("/wallets/{username}")
    public Optional<List<WalletEntity>> getUserAllWallets(@PathVariable String username, @RequestParam String orderId) {
        return payService.getUserAllWallets(username, orderId);
    }

    /**
     * 使用钱包给订单支付
     *
     * @param orderId     订单ID
     * @param walletId    钱包
     * @param payPassword 支付密码
     * @return 支付成功与否
     */
    @PutMapping("/pay")
    public Optional<Boolean> payOrderByWallet(@RequestParam Long walletId, @RequestBody String payPassword, @RequestParam String orderId) {
        return payService.payOrderByWallet(walletId, payPassword, orderId);
    }
}
