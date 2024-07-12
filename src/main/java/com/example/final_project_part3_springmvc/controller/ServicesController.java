package com.example.final_project_part3_springmvc.controller;

import com.example.final_project_part3_springmvc.dto.services.ServicesSaveRequest;
import com.example.final_project_part3_springmvc.dto.services.ServicesSaveResponse;
import com.example.final_project_part3_springmvc.mapper.ServicesMapper;
import com.example.final_project_part3_springmvc.model.Services;
import com.example.final_project_part3_springmvc.service.ServicesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/services")
public class ServicesController {
    private final ServicesService servicesService;

    @PostMapping("/save-services")
    public ResponseEntity<ServicesSaveResponse> saveServices(@RequestBody ServicesSaveRequest request) {
        Services mappedServices = ServicesMapper.INSTANCE.servicesSaveRequestToModel(request);
        Services savedServices = servicesService.saveServices(mappedServices.getServiceName());
        return new ResponseEntity<>(ServicesMapper.INSTANCE.modelToServiceSaveResponse(savedServices), HttpStatus.CREATED);
    }

    @GetMapping("/get-by-id-services/{id}")
    public ServicesSaveResponse getServicesById(@PathVariable Long id) {
        return ServicesMapper.INSTANCE.modelToServiceSaveResponse(servicesService.findById(id));
    }

    @GetMapping("/get-by-name-Services/{name}")
    public ServicesSaveResponse getServicesByUsername(@PathVariable String name) {
        return ServicesMapper.INSTANCE.modelToServiceSaveResponse(servicesService.findByNameServices(name));}

    @DeleteMapping("/delete-by-services-name/{name}")
    public String deleteService(@PathVariable String name){
        servicesService.deleteByServiceName(name);
        return "deleted Services with username:"+name;
    }
}
