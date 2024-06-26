package com.example.final_project_part3_springmvc.exception;

public class NotValidPasswordException extends RuntimeException {
    public NotValidPasswordException(String message) {
    super(message);
    }
}
