package com.example.final_project_part3_springmvc.controller;

import com.example.final_project_part3_springmvc.dto.wallet.WalletSaveResponse;
import com.example.final_project_part3_springmvc.mapper.WalletMapper;
import com.example.final_project_part3_springmvc.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/wallet")
public class WalletController {

private final WalletService walletService;
    @GetMapping("/get-by-id-wallet/{id}")
    public WalletSaveResponse getWalletById(@PathVariable Long id) {
        return WalletMapper.INSTANCE.modelToWalletSaveResponse(walletService.findById(id));
    }
}
