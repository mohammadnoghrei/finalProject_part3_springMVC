package com.example.final_project_part3_springmvc.mapper;

import com.example.final_project_part3_springmvc.dto.customer.CustomerSaveRequest;
import com.example.final_project_part3_springmvc.dto.customer.CustomerSaveResponse;
import com.example.final_project_part3_springmvc.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class );

    Customer customerSaveRequestToModel(CustomerSaveRequest request);
    CustomerSaveResponse modelToCustomerSaveResponse(Customer customer);
}
