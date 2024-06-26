package com.example.final_project_part3_springmvc.dto.customer;

public record CustomerSaveRequest(String firstname,
                                         String lastname,
                                         String username,
                                          String password,
                                          String nationCode,
                                          String email,
                                          double cardBalance ){
}
