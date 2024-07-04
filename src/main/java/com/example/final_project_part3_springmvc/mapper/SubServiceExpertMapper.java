package com.example.final_project_part3_springmvc.mapper;

import com.example.final_project_part3_springmvc.dto.subservicesexpert.SubServiceExpertSaveRequest;
import com.example.final_project_part3_springmvc.dto.subservicesexpert.SubServiceExpertSaveResponse;
import com.example.final_project_part3_springmvc.model.SubServiceExpert;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubServiceExpertMapper {
    SubServiceExpertMapper INSTANCE = Mappers.getMapper(SubServiceExpertMapper.class );

    SubServiceExpert subServiceExpertSaveRequestToModel(SubServiceExpertSaveRequest request);
    SubServiceExpertSaveResponse modelToSubServiceExpertSaveResponse(SubServiceExpert subServiceExpert);
}
