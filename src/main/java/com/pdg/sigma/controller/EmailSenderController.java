package com.pdg.sigma.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pdg.sigma.service.EmailSenderService;

@RestController
public class EmailSenderController {

    @Autowired
    private EmailSenderService emailSenderService;

    @GetMapping("/send-email")
    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String body) {
        try {
            emailSenderService.sendEmail(to, subject, body);
            return "Email sent successfully to " + to;
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}
