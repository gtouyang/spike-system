package com.ogic.spikesystemapi.service;

import com.ogic.spikesystemapi.entity.WalletEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@FeignClient(value = "SPIKE-SYSTEM-PAY-SERVICE")
public interface PayExposeService {

    /**
     * 获得该用户的所有钱包
     *
     * @param username 用户名
     * @return 钱包列表
     */
    @GetMapping("/wallets")
    Optional<List<WalletEntity>> getUserAllWallets(@RequestParam String username);

    /**
     * 获得可以支付的钱包列表
     *
     * @param username 用户名
     * @param orderId  订单ID
     * @return 钱包列表
     */
    @GetMapping("/wallets/payable")
    Optional<List<WalletEntity>> getUserAllWallets(@RequestParam String username, @RequestParam long orderId);

    /**
     * 使用钱包给订单支付
     *
     * @param orderId     订单ID
     * @param wallerId    钱包
     * @param payPassword 支付密码
     * @return 支付成功与否
     */
    @PutMapping("/pay")
    Optional<Boolean> payOrderByWallet(@RequestParam long wallerId, @RequestParam String payPassword, @RequestParam String orderId);

    /**
     * 添加钱包
     * @param wallet
     * @return
     */
    @PostMapping("/wallet")
    Optional<Integer> addWallet(@RequestBody WalletEntity wallet);
}
