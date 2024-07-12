package com.example.final_project_part3_springmvc.controller;

import com.example.final_project_part3_springmvc.dto.subservicesexpert.SubServiceExpertSaveResponse;
import com.example.final_project_part3_springmvc.mapper.SubServiceExpertMapper;
import com.example.final_project_part3_springmvc.model.SubServiceExpert;
import com.example.final_project_part3_springmvc.service.SubServiceExpertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/subServicesExpert")
public class SubServiceExpertController {
    private final SubServiceExpertService subServiceExpertService;

    @PostMapping("/save-subServices/{subServiceName}/{expertUsername}")
    public ResponseEntity<SubServiceExpertSaveResponse> saveSubServiceExpert(@PathVariable String subServiceName, @PathVariable String expertUsername) {
        SubServiceExpert subServiceExpert = subServiceExpertService.saveSubServiceExpert(subServiceName, expertUsername);
        return new ResponseEntity<>(SubServiceExpertMapper.INSTANCE.modelToSubServiceExpertSaveResponse(subServiceExpert), HttpStatus.CREATED);
    }

    @GetMapping("/get-SubServiceExpert-by-name-sub-Services-name-expert/{subServiceName}/{expertUsername}")
    public SubServiceExpertSaveResponse getSubServiceExpertByExpertAndSubService(@PathVariable String subServiceName,@PathVariable String expertUsername ) {
        return SubServiceExpertMapper.INSTANCE.modelToSubServiceExpertSaveResponse(subServiceExpertService.findBySubServiceAndExpert(subServiceName,expertUsername));}

    @DeleteMapping("/delete-by-sub-services-name-expert-username/{subServiceName}/{expertUsername}")
    public String deleteSubServiceExpert(@PathVariable String subServiceName, @PathVariable String expertUsername){
        subServiceExpertService.deleteBySubServiceAndExpert(subServiceName,expertUsername);
        return "deleted SubServicesExpert with expert:"+expertUsername+" subService:"+subServiceName;
    }
}
