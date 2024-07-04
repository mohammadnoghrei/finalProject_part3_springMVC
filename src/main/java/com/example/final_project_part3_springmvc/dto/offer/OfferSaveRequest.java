package com.example.final_project_part3_springmvc.dto.offer;

import com.example.final_project_part3_springmvc.dto.expert.ExpertSaveResponse;
import com.example.final_project_part3_springmvc.dto.order.OrderSaveResponse;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record OfferSaveRequest(
        OrderSaveResponse order,
        ExpertSaveResponse expert,
        double price,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime startOfferDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime endOfferDate) {
}
