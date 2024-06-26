package com.example.final_project_part3_springmvc.dto.subservices;

import com.example.final_project_part3_springmvc.dto.services.ServicesSaveResponse;


public record SubServiceSaveRequest(String name,
                                    ServicesSaveResponse services,
                                    double basePrice,
                                    String description ) {
}
