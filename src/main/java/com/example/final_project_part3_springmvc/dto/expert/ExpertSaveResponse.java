package com.example.final_project_part3_springmvc.dto.expert;

public record ExpertSaveResponse(long id,
                                 String firstname,
                                 String lastname,
                                 String username,
                                 String nationCode,
                                 String email,
                                 double cardBalance) {
}
