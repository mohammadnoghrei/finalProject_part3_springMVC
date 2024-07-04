package com.example.final_project_part3_springmvc.dto.wallet;

public record WalletSaveRequest(
         String cardNumber,
         String cvv,
         String month,
         String year,
         String password,
         double balance) {
}
