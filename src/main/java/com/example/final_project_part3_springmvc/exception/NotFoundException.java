package com.example.final_project_part3_springmvc.exception;
public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
