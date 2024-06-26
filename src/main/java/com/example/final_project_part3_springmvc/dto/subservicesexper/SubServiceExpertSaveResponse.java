package com.example.final_project_part3_springmvc.dto.subservicesexper;

import com.example.final_project_part3_springmvc.dto.expert.ExpertSaveResponse;
import com.example.final_project_part3_springmvc.dto.subservices.SubServicesSaveResponse;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

public record SubServiceExpertSaveResponse(Long id,
                                           SubServicesSaveResponse subServices,
                                           ExpertSaveResponse expert,
                                           LocalDate registerDate) {
}
