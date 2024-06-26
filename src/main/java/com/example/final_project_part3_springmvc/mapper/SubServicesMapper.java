package com.example.final_project_part3_springmvc.mapper;

import com.example.final_project_part3_springmvc.dto.subservices.SubServiceSaveRequest;
import com.example.final_project_part3_springmvc.dto.subservices.SubServicesSaveResponse;
import com.example.final_project_part3_springmvc.model.SubServices;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubServicesMapper {
    SubServicesMapper INSTANCE = Mappers.getMapper(SubServicesMapper.class );

    SubServices subServicesSaveRequestToModel(SubServiceSaveRequest request);
    SubServicesSaveResponse modelToUserSaveResponse(SubServices subServices);
}
