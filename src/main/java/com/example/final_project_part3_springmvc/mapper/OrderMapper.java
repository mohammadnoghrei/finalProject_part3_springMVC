package com.example.final_project_part3_springmvc.mapper;

import com.example.final_project_part3_springmvc.dto.order.OrderSaveRequest;
import com.example.final_project_part3_springmvc.dto.order.OrderSaveResponse;
import com.example.final_project_part3_springmvc.model.Offer;
import com.example.final_project_part3_springmvc.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class );

    Order orderSaveRequestToModel(OrderSaveRequest request);
    OrderSaveResponse modelToOrderSaveResponse(Order order);

}
