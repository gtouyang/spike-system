package com.ogic.spikesystemapi.service;

import com.ogic.spikesystemapi.entity.WalletEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@FeignClient(value = "SPIKE-SYSTEM-SQL-SERVICE")
public interface WalletSqlExposeService {

    /**
     * 根据ID查询钱包
     * @param id
     * @return
     */
    @GetMapping(value = "/sql/select/wallet/{id}")
    Optional<WalletEntity> getWalletById(@PathVariable Long id);

    /**
     * 根据用户名获取钱包列表
     * @param username
     * @return
     */
    @GetMapping(value = "sql/select/wallets/{username}")
    Optional<List<WalletEntity>> getWalletByUsername(@PathVariable String username);

    /**
     * 更新钱包余额
     * @param id
     * @param money
     * @param version
     * @return
     */
    @PostMapping(value = "sql/update/wallet/money")
    Optional<Integer> updateWalletMoney(@RequestParam Long id, @RequestParam Double money, @RequestParam Integer version);

    /**
     * 插入新钱包
     * @param wallet
     */
    @PutMapping(value = "sql/insert/wallet")
    Optional<Integer> insertWallet(@RequestParam WalletEntity wallet);
}
