package com.example.final_project_part3_springmvc.service;

import com.example.final_project_part3_springmvc.exception.DuplicateInformationException;
import com.example.final_project_part3_springmvc.exception.InvalidEntityException;
import com.example.final_project_part3_springmvc.exception.NotFoundException;
import com.example.final_project_part3_springmvc.exception.NotValidPasswordException;
import com.example.final_project_part3_springmvc.model.Customer;
import com.example.final_project_part3_springmvc.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
     Validator validator = validatorFactory.getValidator();


    public boolean validate(Customer entity) {

        Set<ConstraintViolation<Customer>> violations = validator.validate(entity);
        if (violations.isEmpty())
            return true;
        else {
            log.error("Invalid user data found:");
            for (ConstraintViolation<Customer> violation : violations) {
                log.error(violation.getMessage());
            }
            return false;
        }
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("the entity with %s not found", id)));
    }

    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("the entity with %s not found", username)));
    }

    public Customer registerCustomer(Customer customer) {
        customer.setRegisterDate(LocalDate.now());
        if (customerRepository.findByUsername(customer.getUsername()).isPresent())
            throw new DuplicateInformationException(String.format("the customer with %s is duplicate", customer.getUsername()));
        if  (!validate(customer)) {
            throw new InvalidEntityException(String.format("the customer with %s have invalid variable", customer.getUsername()));
        }
        return customerRepository.save(customer);
    }


    public void deleteByUsername(String username) {
        customerRepository.delete(findByUsername(username));
    }

    @Transactional
    public void updatePassword(String username, String oldPassword, String newPassword, String finalNewPassword) {
        String passRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        if (customerRepository.findByUsername(username).isEmpty()) {
            throw new NotFoundException(String.format("the entity with %s not found", username));
        } else if (!newPassword.matches(passRegex) || !oldPassword.matches(passRegex) || !finalNewPassword.matches(passRegex)) {
            throw new NotValidPasswordException("this password not valid");
        } else if (!findByUsername(username).getPassword().equals( oldPassword )|| !newPassword.equals(finalNewPassword)) {
            throw new NotValidPasswordException("this password not valid");
        } else {
            customerRepository.updatePassword(finalNewPassword, username);
        }
    }

}
