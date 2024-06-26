package com.example.final_project_part3_springmvc.exception;


import com.example.final_project_part3_springmvc.dto.exception.ExceptionDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateInformationException.class)
    public ResponseEntity<ExceptionDto> duplicateInformationExceptionHandler(DuplicateInformationException e) {
        log.warn(e.getMessage());
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDto> constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.warn(e.getMessage());
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionDto, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(ConfirmationException.class)
    public ResponseEntity<ExceptionDto> confirmationExceptionHandler(ConfirmationException e) {
        log.warn(e.getMessage());
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionDto, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<ExceptionDto> invalidEntityExceptionHandler(InvalidEntityException e) {
        log.warn(e.getMessage());
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionDto, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> notFoundExceptionHandler(NotFoundException e) {
        log.warn(e.getMessage());
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionDto, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(NotValidPasswordException.class)
    public ResponseEntity<ExceptionDto> notValidPasswordExceptionHandler(NotValidPasswordException e) {
        log.warn(e.getMessage());
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionDto, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(NullListException.class)
    public ResponseEntity<ExceptionDto> nullListExceptionHandler(NullListException e) {
        log.warn(e.getMessage());
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionDto, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(StatusException.class)
    public ResponseEntity<ExceptionDto> statusExceptionHandler(StatusException e) {
        log.warn(e.getMessage());
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionDto, HttpStatus.PRECONDITION_FAILED);
    }
}
