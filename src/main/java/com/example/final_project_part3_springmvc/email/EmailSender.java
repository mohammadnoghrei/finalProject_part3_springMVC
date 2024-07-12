package com.example.final_project_part3_springmvc.email;

import org.springframework.stereotype.Component;

//@Component
public interface EmailSender {
    void send(String to, String email);
}
