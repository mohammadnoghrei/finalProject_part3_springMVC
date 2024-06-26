package com.example.final_project_part3_springmvc.service;

import com.example.final_project_part3_springmvc.exception.DuplicateInformationException;
import com.example.final_project_part3_springmvc.exception.InvalidEntityException;
import com.example.final_project_part3_springmvc.exception.NotFoundException;
import com.example.final_project_part3_springmvc.model.SubServices;
import com.example.final_project_part3_springmvc.repository.SubServicesRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@RequiredArgsConstructor
@Service
public class SubServicesService {
    private final SubServicesRepository subServicesRepository;
    private final ServicesService servicesService;
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();


    public boolean validate(SubServices entity) {

        Set<ConstraintViolation<SubServices>> violations = validator.validate(entity);
        if (violations.isEmpty())
            return true;
        else {
            System.out.println("Invalid user data found:");
            for (ConstraintViolation<SubServices> violation : violations) {
                System.out.println(violation.getMessage());}
            return false;}
    }

    public List<SubServices> findAllSubServiceOfService(String serviceName) {
       return subServicesRepository.findAllByServices(servicesService.findByNameServices(serviceName));
    }

    public SubServices saveSubService(SubServices subServices, String serviceName) {
        subServices.setServices(servicesService.findByNameServices(serviceName));
        if (!validate(subServices))
            throw new InvalidEntityException(String.format("the customer with %s have invalid variable", subServices.getName()));
        else if (subServicesRepository.findByName(subServices.getName()).isPresent()) {
            throw new DuplicateInformationException(String.format("the sub service with %s is duplicate", subServices.getName()));}
        return subServicesRepository.save(subServices);}
    public SubServices findById(Long id) {
        return subServicesRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("the entity with %s not found", id)));
    }
    public SubServices findBySubServiceName(String name) {
        return subServicesRepository.findByName(name).orElseThrow(() -> new NotFoundException(String.format("the entity with %s not found", name)));
    }
    public void deleteByName(String name) {
        subServicesRepository.delete(findBySubServiceName(name));
    }
    @Transactional
    public void updateDescription(String description, String name) {
        if (subServicesRepository.findByName(name).isEmpty())
            throw new NotFoundException(String.format("not found any sub service with %s  ", name));
        else subServicesRepository.updateDescription(description, name);
    }
    @Transactional
    public void updateBasePrice(double basePrice, String name) {
        if (subServicesRepository.findByName(name).isEmpty())
            throw new NotFoundException(String.format("not found any sub service with %s  ", name));
        else if (basePrice<0){
            throw new InvalidEntityException(String.format("the customer with %s have invalid price", name));}
        else subServicesRepository.updateBasePrice(basePrice, name);
    }
}
