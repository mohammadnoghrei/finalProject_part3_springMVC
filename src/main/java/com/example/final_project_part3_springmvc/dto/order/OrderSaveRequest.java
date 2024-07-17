package com.example.final_project_part3_springmvc.dto.order;

import com.example.final_project_part3_springmvc.dto.customer.CustomerSaveResponse;
import com.example.final_project_part3_springmvc.dto.subservices.SubServicesSaveResponse;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record OrderSaveRequest(CustomerSaveResponse customer,
                               SubServicesSaveResponse subServices,
                               double customerOfferPrice,
                               @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
                               LocalDateTime requestedDateToDoOrder,
                               String address) {
}
