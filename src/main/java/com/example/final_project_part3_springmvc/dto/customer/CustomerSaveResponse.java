package com.example.final_project_part3_springmvc.dto.customer;

public record CustomerSaveResponse(long id,
                                   String firstname,
                                   String lastname,
                                   String username,
                                   String nationCode,
                                   String email,
                                   double cardBalance) {
}
