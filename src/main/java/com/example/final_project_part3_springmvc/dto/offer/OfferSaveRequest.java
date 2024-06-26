package com.example.final_project_part3_springmvc.dto.offer;

import com.example.final_project_part3_springmvc.dto.expert.ExpertSaveResponse;
import com.example.final_project_part3_springmvc.dto.order.OrderSaveResponse;

public record OfferSaveRequest(
        OrderSaveResponse order,
        ExpertSaveResponse expert,
        double price,
        boolean confirmed) {
}
