package com.example.final_project_part3_springmvc.mapper;

import com.example.final_project_part3_springmvc.dto.expert.ExpertSaveRequest;
import com.example.final_project_part3_springmvc.dto.expert.ExpertSaveResponse;
import com.example.final_project_part3_springmvc.model.Expert;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExpertMapper {
    ExpertMapper INSTANCE = Mappers.getMapper(ExpertMapper.class );

    Expert expertSaveRequestToModel(ExpertSaveRequest request);
    ExpertSaveResponse modelToExpertSaveResponse(Expert expert);
}
