package com.pdg.sigma.controller;

import java.util.List;

import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.service.MonitorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pdg.sigma.service.EmailSenderService;

@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
@RestController
public class EmailSenderController {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private MonitorServiceImpl monitorService;

    @GetMapping("/send-basic-email")
    public String sendBasicEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String body) {
        try {
            emailSenderService.sendEmail(to, subject, body);
            return "Email sent successfully to " + to;
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }

    // @PostMapping("/email-finish-selection-to-professor")
    // public ResponseEntity<String> sendEmailFinishToProfessor(@RequestBody List<String> electedApplicantCodes) {
    //     try {
            
    //         List<Monitor> allApplicants = studentService.findAll();

    //         if (allApplicants == null || allApplicants.isEmpty()) {
    //             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No applicants found in the database");
    //         }

    //         List<Monitor> electedApplicants = allApplicants.stream()
    //                 .filter(applicant -> electedApplicantCodes.contains(applicant.getCode())).collect(Collectors.toList());

    //         if (electedApplicants.isEmpty()) {
    //             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No elected applicants found with the provided codes");
    //         }
    //         else{
    //             StringBuilder emailContent = new StringBuilder("Elected Applicants:\n\n");
    //             for (Monitor applicant : electedApplicants) {
    //                 emailContent.append(String.format("Name: %s %s, Code: %s, Average Grade: %.2f\n",
    //                         applicant.getName(), applicant.getLastName(), applicant.getCode(), applicant.getGradeAverage()));
    //             }
    
    //             String subject = "Selected Aplicants of your Monitoring";
    //             String recipient = "juandiegoloralara@gmail.com"; // REPLACE
    //             emailSenderService.sendHtmlEmail(recipient, subject, emailContent.toString());
    
    //             return ResponseEntity.ok("Email sent successfully!");
    //         }

            
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
    //     }
    // }

    @PostMapping("/email-finish-selection")
    public ResponseEntity<String> sendEmailFinishSelection(@RequestBody List<String> electedApplicantCodes) {

        try {

            List<Monitor> allApplicants = monitorService.findAll();

            if (allApplicants == null || allApplicants.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No applicants found for this monitor process.");
            }

            int emailsSentCount = 0;
            int electedNotifiedCount = 0;
            int nonElectedNotifiedCount = 0;

            StringBuilder processingSummary = new StringBuilder("Email process summary:\n");

            for (Monitor applicant : allApplicants) {
                String subject;
                String body;
                String recipientEmail = applicant.getEmail(); // Obtener el email del postulante

                if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
                    System.out.println("Skipping email for applicant " + applicant.getCode() + " - No email provided."); // Usa tu logger preferido
                    processingSummary.append("- Skipped email for " + applicant.getName() + " " + applicant.getLastName() + " (Code: " + applicant.getCode() + "): No email provided.\n");
                    continue; // Saltar a la siguiente iteración si no hay email

                }

                boolean isElected = electedApplicantCodes != null && electedApplicantCodes.contains(applicant.getCode());

                if (isElected) {
                    subject = "¡Felicidades! Fuiste seleccionado como Monitor/a";
                    body = String.format(
                        "<html><body>" +
                        "<p>Hola %s,</p>" +
                        "<p>¡Felicidades! Has sido seleccionado para ser el Monitor/a</p>" + 
                        "<p>Tu dedicación y promedio (%.2f) han sido reconocidos.</p>" + 
                        "<p>El profesor de la materia se pondrá en contacto contigo pronto para coordinar los detalles y próximos pasos.</p>" +
                        "<p>¡Mucho éxito en esta nueva experiencia!</p>" +
                        "</body></html>",

                        applicant.getName(),
                        applicant.getGradeCourse()

                    );

                    electedNotifiedCount++;
                    processingSummary.append("- Sent 'Elected' email to " + applicant.getName() + " " + applicant.getLastName() + " (Code: " + applicant.getCode() + ").\n");

                } else {
                    subject = "Actualización sobre tu postulación a Monitoria ";
                    body = String.format(
                        "<html><body>" +
                        "<p>Hola %s,</p>" +
                        "<p>Gracias por tu participación en la convocatoria</p>" +
                        "<p>Valoramos mucho tu interés y el tiempo que dedicaste en postularte.</p>" +
                        "<p>Queremos informarte que, si bien tu perfil es sumamente valioso y agradecemos enormemente tu interés (tu promedio es %.2f), en esta ocasión no has sido seleccionado para esta monitoria específica.</p>" + // Opcional: menciona promedio
                        "<p>La selección se basó en diversos criterios y las necesidades específicas de la materia en este periodo.</p>" +
                        "<p>Te animamos a seguir explorando otras oportunidades dentro de la universidad y a postularte en futuras convocatorias.</p>" +
                        "<p>Gracias nuevamente por ser parte de nuestra comunidad académica.</p>" +
                        "</body></html>",

                        applicant.getName(),
                        applicant.getGradeCourse()
                    );

                    nonElectedNotifiedCount++;
                    processingSummary.append("- Sent 'Not Elected' email to " + applicant.getName() + " " + applicant.getLastName() + " (Code: " + applicant.getCode() + ").\n");
                }

                emailSenderService.sendHtmlEmail(recipientEmail, subject, body);
                emailsSentCount++;
            }

            String responseMessage = String.format(
                "Email process finished. %d emails attempted (%d elected, %d non-elected). Check server logs for details.",
                emailsSentCount,
                electedNotifiedCount,
                nonElectedNotifiedCount
            );
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            System.err.println("Error during email finish selection process: " + e.getMessage());
            e.printStackTrace(); 

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during email process: " + e.getMessage());
        }

    }

    // To try it, by GET
    //http://localhost:5433/send-basic-email?to=any@gmail.com&subject=Hello&body=Test Email
}
