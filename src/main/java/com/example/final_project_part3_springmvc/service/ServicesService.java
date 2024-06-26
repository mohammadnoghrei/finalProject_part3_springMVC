package com.example.final_project_part3_springmvc.service;

import com.example.final_project_part3_springmvc.exception.DuplicateInformationException;
import com.example.final_project_part3_springmvc.exception.NotFoundException;
import com.example.final_project_part3_springmvc.model.Services;
import com.example.final_project_part3_springmvc.repository.ServicesRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;


@RequiredArgsConstructor
@Service
public class ServicesService  {
    private final ServicesRepository servicesRepository;
     ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
     Validator validator = validatorFactory.getValidator();

    public boolean validate(Services entity) {

        Set<ConstraintViolation<Services>> violations = validator.validate(entity);
        if (violations.isEmpty())
            return true;
        else {
            System.out.println("Invalid user data found:");
            for (ConstraintViolation<Services> violation : violations) {
                System.out.println(violation.getMessage());
            }
            return false;
        }
    }
    public Services saveServices(String serviceName){
        if (servicesRepository.findByServiceName(serviceName).isPresent())
            throw new DuplicateInformationException(String.format("the service with %s is duplicate", serviceName));
        Services services=new Services();
        services.setServiceName(serviceName);
        return servicesRepository.save(services);
    }
    public Services findByNameServices(String name){
        return servicesRepository.findByServiceName(name).orElseThrow(()-> new NotFoundException(String.format("the entity with %s not found",name)));
    }
    public Services findById(Long id) {
        return servicesRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("the entity with %s not found", id)));
    }


    public void deleteByServiceName(String name) {
        servicesRepository.delete(findByNameServices(name));
    }

}
