package com.fsre.attendance_tracker_backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendCredentialsEmail(String to, String username, String password) {
        String messageBody = String.format(
                "Vaši podaci za prijavu:\nUsername: %s\nPassword: %s",
                username,
                password
        );

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("slavko.ramljak@fsre.sum.ba");
        message.setTo(to);
        message.setSubject("Podaci za prijavu");
        message.setText(messageBody);

        mailSender.send(message);
    }

}
