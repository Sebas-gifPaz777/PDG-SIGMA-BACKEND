package com.pdg.sigma.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pdg.sigma.domain.Candidature;
import com.pdg.sigma.service.CandidatureServiceImpl;
import com.pdg.sigma.service.EmailSenderService;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class EmailSenderController {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private CandidatureServiceImpl candidatureService;

    @GetMapping("/send-basic-email")
    public String sendBasicEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String body) {
        try {
            emailSenderService.sendEmail(to, subject, body);
            return "Email sent successfully to " + to;
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }

    @PostMapping("/email-finish-selection")
    public ResponseEntity<String> sendEmailFinishSelection(@RequestBody List<String> electedApplicantCodes) {
        try {
            
            List<Candidature> allApplicants = candidatureService.findAll();

            if (allApplicants == null || allApplicants.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No applicants found in the database");
            }

            List<Candidature> electedApplicants = allApplicants.stream()
                    .filter(applicant -> electedApplicantCodes.contains(applicant.getCode())).collect(Collectors.toList());

            if (electedApplicants.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No elected applicants found with the provided codes");
            }
            else{
                StringBuilder emailContent = new StringBuilder("Elected Applicants:\n\n");
                for (Candidature applicant : electedApplicants) {
                    emailContent.append(String.format("Name: %s %s, Code: %s, Average Grade: %.2f\n",
                            applicant.getName(), applicant.getLastName(), applicant.getCode(), applicant.getGradeAverage()));
                }
    
                String subject = "Selected Aplicants of your Monitoring";
                String recipient = "juandiegoloralara@gmail.com"; // REPLACE
                emailSenderService.sendHtmlEmail(recipient, subject, emailContent.toString());
    
                return ResponseEntity.ok("Email sent successfully!");
            }

            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


    // To try it, by GET
    //http://localhost:5433/send-basic-email?to=any@gmail.com&subject=Hello&body=Test Email
}
