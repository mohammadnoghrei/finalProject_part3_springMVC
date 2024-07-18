package com.example.final_project_part3_springmvc.email;

import com.example.final_project_part3_springmvc.exception.ConfirmationException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class EmailService implements EmailSender{

    private final JavaMailSender javaMailSender;



    @Async
    @Override
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage=javaMailSender.createMimeMessage();
            MimeMessageHelper helper= new MimeMessageHelper(mimeMessage,"utf-8");
            helper.setText(email,true);
            helper.setTo(to);
            helper.setSubject("Confirm Your Email");
            helper.setFrom("mohammad.noghrei.python@gmail.com");
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("failed to send email",e);
            throw new ConfirmationException("failed to send email");
        }
    }
}
