package com.example.final_project_part3_springmvc.service;

import com.example.final_project_part3_springmvc.exception.InvalidEntityException;
import com.example.final_project_part3_springmvc.exception.NotFoundException;
import com.example.final_project_part3_springmvc.model.Wallet;
import com.example.final_project_part3_springmvc.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service

public class WalletService {
    private final WalletRepository walletRepository;


    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet findByCardNumber(String cardNumber) {
        return walletRepository.findByCardNumber(cardNumber).orElseThrow(() -> new NotFoundException(String.format("the entity with %s not found", cardNumber)));
    }

    public Wallet findById(long id) {
        return walletRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("the entity with %s not found", id)));
    }

    public Wallet walletCheck(Wallet wallet) {
        Wallet findWallet = findByCardNumber(wallet.getCardNumber());
        if (!findWallet.getPassword().equals(wallet) || !findWallet.getYear().equals(wallet.getYear()) ||
                !findWallet.getMonth().equals(wallet.getMonth()) || !findWallet.getCvv().equals(wallet.getCvv()))
            throw new InvalidEntityException("the parameters of entity not valid ");
        return findWallet;
    }

    public void updateBalance(long walletId, double balance) {
        Wallet byId = findById(walletId);
        byId.setBalance(balance);
        walletRepository.save(byId);
    }
}
