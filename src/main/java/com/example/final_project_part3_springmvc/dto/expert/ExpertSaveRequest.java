package com.example.final_project_part3_springmvc.dto.expert;

public record ExpertSaveRequest(String firstname,
                                String lastname,
                                String username,
                                String password,
                                String nationCode,
                                String email,
                                double cardBalance) {
}
