package com.example.final_project_part3_springmvc.mapper;

import com.example.final_project_part3_springmvc.dto.services.ServicesSaveRequest;
import com.example.final_project_part3_springmvc.dto.services.ServicesSaveResponse;
import com.example.final_project_part3_springmvc.model.Services;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ServicesMapper {
    ServicesMapper INSTANCE = Mappers.getMapper(ServicesMapper.class );

    Services servicesSaveRequestToModel(ServicesSaveRequest request);
    ServicesSaveResponse modelToServiceSaveResponse(Services services);
}
