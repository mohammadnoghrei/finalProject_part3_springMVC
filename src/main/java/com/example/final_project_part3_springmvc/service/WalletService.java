package com.example.final_project_part3_springmvc.service;

import com.example.final_project_part3_springmvc.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class WalletService  {
    private final WalletRepository walletRepository;
}
