package com.example.final_project_part3_springmvc.exception;

public class InvalidEntityException extends RuntimeException {

    public InvalidEntityException(String message){
        super(message);
    }
}
