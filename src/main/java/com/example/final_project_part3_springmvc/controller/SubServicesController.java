package com.example.final_project_part3_springmvc.controller;

import com.example.final_project_part3_springmvc.dto.subservices.SubServiceSaveRequest;
import com.example.final_project_part3_springmvc.dto.subservices.SubServicesSaveResponse;
import com.example.final_project_part3_springmvc.mapper.SubServicesMapper;
import com.example.final_project_part3_springmvc.model.SubServices;
import com.example.final_project_part3_springmvc.service.SubServicesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SubServicesController {
    private final SubServicesService subServicesService;

    @PostMapping("/save-subServices/{serviceName}")
    public ResponseEntity<SubServicesSaveResponse> saveSubServices(@RequestBody SubServiceSaveRequest request, @PathVariable String serviceName) {
        SubServices mappedSubServices = SubServicesMapper.INSTANCE.subServicesSaveRequestToModel(request);
        SubServices savedSubServices = subServicesService.saveSubService(mappedSubServices,serviceName);
        return new ResponseEntity<>(SubServicesMapper.INSTANCE.modelToUserSaveResponse(savedSubServices), HttpStatus.CREATED);
    }

    @GetMapping("/get-by-id-sub-services/{id}")
    public SubServicesSaveResponse getSubServicesById(@PathVariable Long id) {
        return SubServicesMapper.INSTANCE.modelToUserSaveResponse(subServicesService.findById(id));
    }

    @GetMapping("/get-by-name-sub-Services/{name}")
    public SubServicesSaveResponse getSubServicesByUsername(@PathVariable String name) {
        return SubServicesMapper.INSTANCE.modelToUserSaveResponse(subServicesService.findBySubServiceName(name));}

    @DeleteMapping("/delete-by-sub-services-name/{name}")
    public String deleteSubService(@PathVariable String name){
        subServicesService.deleteByName(name);
        return "deleted SubServices with username:"+name;
    }

    @RequestMapping("/change-sub-service-description/{name}/{description}")
    public String changeSubServiceDescription(@PathVariable String name, @PathVariable String description) {
        subServicesService.updateDescription(description,name);
        return "change subService description with name: "+name;
    }

    @RequestMapping("/change-sub-service-price/{name}/{price}")
    public String changeSubServiceBasePrice(@PathVariable String name, @PathVariable double price) {
        subServicesService.updateBasePrice(price,name);
        return "change subService basePrice with name: "+name;
    }
}
