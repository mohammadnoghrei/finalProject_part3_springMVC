package com.example.final_project_part3_springmvc.dto.order;

import com.example.final_project_part3_springmvc.dto.customer.CustomerSaveResponse;
import com.example.final_project_part3_springmvc.dto.subservices.SubServicesSaveResponse;
import com.example.final_project_part3_springmvc.model.Customer;
import com.example.final_project_part3_springmvc.model.SubServices;

import java.time.LocalDate;

public record OrderSaveRequest(CustomerSaveResponse customer,
                               SubServicesSaveResponse subServices,
                               double customerOfferPrice,
                               LocalDate requestedDateToDoOrder,
                               String address) {
}
