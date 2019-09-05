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
    @GetMapping("/wallets")
    public Optional<List<WalletEntity>> getUserAllWallets(@RequestParam String username) {
        return Optional.ofNullable(payService.getUserAllWallets(username));
    }

    /**
     * 获得可以支付的钱包列表
     *
     * @param username 用户名
     * @param orderId  订单ID
     * @return 钱包列表
     */
    @GetMapping("/wallets/payable")
    public Optional<List<WalletEntity>> getUserAllWallets(@RequestParam String username, @RequestParam long orderId) {
        return Optional.ofNullable(payService.getUserAllWallets(username, orderId));
    }


    /**
     * 新建钱包
     * @param wallet
     * @return
     */
    @PostMapping("/wallet")
    public Optional<Integer> addWallet(@RequestBody WalletEntity wallet) {
        return Optional.ofNullable(payService.addWallet(wallet));
    }
}
