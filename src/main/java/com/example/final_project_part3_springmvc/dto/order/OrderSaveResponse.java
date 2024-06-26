package com.example.final_project_part3_springmvc.dto.order;

import com.example.final_project_part3_springmvc.dto.customer.CustomerSaveResponse;
import com.example.final_project_part3_springmvc.dto.subservices.SubServicesSaveResponse;

import java.time.LocalDate;

public record OrderSaveResponse(long id,
        CustomerSaveResponse customer,
                                SubServicesSaveResponse subServices,
                                double customerOfferPrice,
                                LocalDate requestedDateToDoOrder,
                                String address) {
}
