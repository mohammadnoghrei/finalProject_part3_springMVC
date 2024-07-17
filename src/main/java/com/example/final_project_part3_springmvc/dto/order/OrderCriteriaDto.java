package com.example.final_project_part3_springmvc.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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

   public String getCustomerUserName() {
      return this.customerUserName;
   }

   public String getExpertUserName() {
      return this.expertUserName;
   }

   public String getStatus() {
      return this.status;
   }

   public String getSubServiceName() {
      return this.subServiceName;
   }

   public String getServiceName() {
      return this.serviceName;
   }

   public LocalDateTime getStartDate() {
      return this.startDate;
   }

   public LocalDateTime getEndDate() {
      return this.endDate;
   }
}
