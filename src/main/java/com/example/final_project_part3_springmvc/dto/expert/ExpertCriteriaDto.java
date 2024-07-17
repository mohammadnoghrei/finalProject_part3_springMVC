package com.example.final_project_part3_springmvc.dto.expert;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ExpertCriteriaDto {
    public String firstname;
    public String lastname;
    public String email;
    public int orderCount;
    public int rate;
    public String subServiceName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime minRegisterDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime maxRegisterDate;
}
