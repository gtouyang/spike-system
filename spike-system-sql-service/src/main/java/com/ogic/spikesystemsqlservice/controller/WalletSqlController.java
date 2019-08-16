package com.ogic.spikesystemsqlservice.controller;

import com.ogic.spikesystemapi.entity.WalletEntity;
import com.ogic.spikesystemsqlservice.mapper.WalletMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@Controller
public class WalletSqlController {

    @Autowired
    WalletMapper walletMapper;

    @GetMapping(value = "/sql/select/wallet/{id}")
    public Optional<WalletEntity> getWalletById(@PathVariable Long id){
        return Optional.ofNullable(walletMapper.getWalletById(id));
    }

    @GetMapping(value = "sql/select/wallets/{username}")
    public Optional<List<WalletEntity>> getWalletByUsername(@PathVariable String username){
        return Optional.ofNullable(walletMapper.getWalletByUsername(username));
    }

    @PostMapping(value = "sql/update/wallet/money")
    public Optional<Integer> updateWalletMoney(@RequestParam Long id, @RequestParam Double money, @RequestParam Integer version){
        return Optional.ofNullable(walletMapper.updateWalletMoney(id, money, version));
    }

    @PutMapping(value = "sql/insert/wallet")
    public Optional<Integer> insertWallet(@RequestParam WalletEntity wallet){
        return Optional.ofNullable(walletMapper.insertWallet(wallet));
    }
}
