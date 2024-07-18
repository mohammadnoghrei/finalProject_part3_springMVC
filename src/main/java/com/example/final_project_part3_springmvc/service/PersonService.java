package com.example.final_project_part3_springmvc.service;


import com.example.final_project_part3_springmvc.model.Person;
import com.example.final_project_part3_springmvc.repository.PersonRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class PersonService  {
    private final PersonRepository personRepository;
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
   Validator validator = validatorFactory.getValidator();

    public boolean validate(Person entity) {

        Set<ConstraintViolation<Person>> violations = validator.validate(entity);
        if (violations.isEmpty())
            return true;
        else {
            System.out.println("Invalid user data found:");
            for (ConstraintViolation<Person> violation : violations) {
                System.out.println(violation.getMessage());
            }
            return false;
        }
    }

    public Optional<Person> findByUsername(String username) {
        return personRepository.findByUsername(username);
    }
    public int enablePerson(String userName) {
        return personRepository.enablePerson(userName);
    }
}
