package com.example.final_project_part3_springmvc.dto.subservicesexpert;

import com.example.final_project_part3_springmvc.dto.expert.ExpertSaveResponse;
import com.example.final_project_part3_springmvc.dto.subservices.SubServicesSaveResponse;

import java.time.LocalDate;

public record SubServiceExpertSaveRequest(SubServicesSaveResponse subServices,
                                          ExpertSaveResponse expert,
                                          LocalDate registerDate) {
}
