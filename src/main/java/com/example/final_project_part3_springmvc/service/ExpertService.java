package com.example.final_project_part3_springmvc.service;


import com.example.final_project_part3_springmvc.exception.*;
import com.example.final_project_part3_springmvc.model.Expert;
import com.example.final_project_part3_springmvc.model.ExpertStatus;
import com.example.final_project_part3_springmvc.repository.ExpertRepository;

import com.example.final_project_part3_springmvc.utility.Util;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpertService {
    private final ExpertRepository expertRepository;

     ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
  Validator validator = validatorFactory.getValidator();


    public boolean validate(Expert entity) {

        Set<ConstraintViolation<Expert>> violations = validator.validate(entity);
        if (violations.isEmpty())
            return true;
        else {log.warn("Invalid user data found:");
            for (ConstraintViolation<Expert> violation : violations) {
                log.warn(violation.getMessage());
            }
            return false;
        }
    }
    public List<Expert> findAllExpertByStatus(ExpertStatus expertStatus){
        return expertRepository.findAllByExpertStatus(expertStatus);
    }
    public Expert registerExpert(Expert expert,String imagePath){
        expert.setExpertStatus(ExpertStatus.NEW);
        expert.setImage(Util.saveImage(imagePath));
        if (expertRepository.findByUsername(expert.getUsername()).isPresent())
            throw new DuplicateInformationException(String.format("the customer with %s is duplicate",expert.getUsername()));
        else if (!validate(expert)) {
            throw new InvalidEntityException(String.format("the customer with %s have invalid variable",expert.getUsername()));
        }else return expertRepository.save(expert);
    }

    public Expert findById(Long id){
        return expertRepository.findById(id).orElseThrow(()-> new NotFoundException(String.format("the entity with %s not found",id)));
    }

    public Expert findByUsername(String username){
        return expertRepository.findByUsername(username).orElseThrow(()-> new NotFoundException(String.format("the entity with %s not found",username)));
    }


    public void deleteByUsername(String username){
        expertRepository.delete(findByUsername(username));
    }
    @Transactional
    public void updatePassword(String username, String oldPassword, String newPassword, String finalNewPassword) {
        String passRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        if (expertRepository.findByUsername(username).isEmpty()) {
            throw new NotFoundException(String.format("the entity with %s not found", username));
        } else if (!newPassword.matches(passRegex) || !oldPassword.matches(passRegex) || !finalNewPassword.matches(passRegex)) {
            throw new NotValidPasswordException("this password not valid");
        } else if (!findByUsername(username).getPassword().equals(oldPassword) || !newPassword.equals(finalNewPassword))
            throw new NotValidPasswordException("this password not valid");
        else {
            expertRepository.updatePassword(finalNewPassword, username);
        }
    }
    @Transactional
    public void confirmExpert(String username){
        if (expertRepository.findByUsername(username).isEmpty())
            throw new NotFoundException(String.format("the entity with %s username not found",username));
        else if (findByUsername(username).getExpertStatus().equals(ExpertStatus.CONFIRMED)) {
            throw new ConfirmationException(String.format("the entity with %s username was confirmed before your confirmation ",username));
        }
        expertRepository.confirmExpert(ExpertStatus.CONFIRMED,username);
    }

    @Transactional
    public void updateScore(double score, String username){
        if (expertRepository.findByUsername(username).isEmpty())
            throw new NotFoundException(String.format("the entity with %s username not found",username));
        if (score<0)
            throw new InvalidEntityException(String.format("the Expert with %s have invalid variable",username));
        expertRepository.updateScore(score,username);
    }

}
