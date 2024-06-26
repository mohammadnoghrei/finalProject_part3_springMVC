package com.example.final_project_part3_springmvc.exception;

public class DuplicateInformationException extends RuntimeException{
    public DuplicateInformationException (String message){
        super(message);
    }
}
