package com.example.final_project_part3_springmvc.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class OrderCriteriaDto {

   private String customerUserName;
   private String expertUserName;
   private String status;
   private String subServiceName;
   private String serviceName;
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
   private LocalDateTime startDate;
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
   private LocalDateTime endDate;
}
