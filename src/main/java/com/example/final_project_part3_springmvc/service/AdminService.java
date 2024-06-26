package com.example.final_project_part3_springmvc.service;

import com.example.final_project_part3_springmvc.model.Admin;
import com.example.final_project_part3_springmvc.repository.AdminRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService  {
    private final AdminRepository adminRepository;

     ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
   Validator validator = validatorFactory.getValidator();


    public boolean validate(Admin entity) {

        Set<ConstraintViolation<Admin>> violations = validator.validate(entity);
        if (violations.isEmpty())
            return true;
        else {
            log.error("Invalid user data found:");
            for (ConstraintViolation<Admin> violation : violations) {
                log.error(violation.getMessage());
            }
            return false;
        }
    }


}
