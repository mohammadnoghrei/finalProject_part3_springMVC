package com.example.final_project_part3_springmvc.mapper;

import com.example.final_project_part3_springmvc.dto.admin.AdminSaveRequest;
import com.example.final_project_part3_springmvc.dto.admin.AdminSaveResponse;
import com.example.final_project_part3_springmvc.model.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMapper {
    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class );

    Admin adminSaveRequestToModel(AdminSaveRequest request);
    AdminSaveResponse modelToAdminSaveResponse(Admin admin);
}
