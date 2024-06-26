package com.example.final_project_part3_springmvc.dto.exception;

import java.time.LocalDateTime;

public record ExceptionDto (String message,
                           LocalDateTime localDateTime){
}
