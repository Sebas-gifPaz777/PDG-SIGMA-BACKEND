package com.pdg.sigma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.pdg.sigma.domain.MonitoringMonitor;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        //message.setFrom("sigma.appservice@gmail.com");

        mailSender.send(message);
    }

    public void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        mailSender.send(mimeMessage);
    }

    public void sendToMonitors(List<MonitoringMonitor> relations, boolean electedParameterShouldBeUsedOrRemoved) {
        if (relations == null || relations.isEmpty()) {
            System.out.println("EmailSenderService: No hay relaciones para procesar.");
            return;
        }

        for (MonitoringMonitor relation : relations) {
            if (relation.getMonitor() == null || relation.getMonitor().getEmail() == null || relation.getMonitor().getEmail().isEmpty()) {
                System.err.println("EmailSenderService: Monitor o email nulo/vacío para una relación. Skipping.");
                continue;
            }
            if (relation.getMonitoring() == null || relation.getMonitoring().getCourse() == null || relation.getMonitoring().getCourse().getName() == null) {
                System.err.println("EmailSenderService: Monitoring o Course o Course Name nulo para una relación. Skipping email para: " + relation.getMonitor().getEmail());
                continue;
            }


            String email = relation.getMonitor().getEmail();
            String name = relation.getMonitor().getName();
            String estadoActual = relation.getEstadoSeleccion();

            String subject;
            String body;

            boolean esRealmenteSeleccionado = estadoActual != null && "seleccionado".equalsIgnoreCase(estadoActual.trim());

            if (esRealmenteSeleccionado) {
                subject = "¡Felicitaciones! Has sido seleccionado";
                body = "Hola " + name + ",\n\nHas sido seleccionado para la monitoría de "
                    + relation.getMonitoring().getCourse().getName() + ". ¡Felicitaciones! Muchos exitos en este proceso.\n\nSaludos,";
            } else {
                subject = "Resultado del proceso de selección";
                body = "Hola " + name + ",\n\nGracias por tu interés. Lamentamos informarte que no fuiste seleccionado para la monitoría de "
                    + relation.getMonitoring().getCourse().getName() + ".\n\nTe animamos a seguir participando.\n\nSaludos,";
            }
            System.out.println("EmailSenderService: Preparando email para " + email + " con estado: " + estadoActual + " -> esRealmenteSeleccionado: " + esRealmenteSeleccionado);


            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email);
                message.setSubject(subject);
                message.setText(body);
                message.setFrom("sigma.appservice@gmail.com"); 

                mailSender.send(message);
                System.out.println("EmailSenderService: Email enviado a " + email);
            } catch (MailException e) {
                System.err.println("EmailSenderService: MailException al enviar a " + email + " - " + e.getMessage());
            } catch (Exception e) {
                System.err.println("EmailSenderService: Exception general al enviar a " + email);
                e.printStackTrace();
            }
        }
    }


}