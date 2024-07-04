package com.example.final_project_part3_springmvc.repository;


import com.example.final_project_part3_springmvc.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface WalletRepository extends JpaRepository<Wallet,Long> {
    Optional<Wallet> findByCardNumber(String cardNumber);
}
