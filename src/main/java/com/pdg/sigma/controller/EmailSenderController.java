package com.pdg.sigma.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.domain.MonitoringMonitor;
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
import com.pdg.sigma.dto.SelectionResultDTO;
import com.pdg.sigma.repository.MonitoringMonitorRepository;

@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
@RestController
public class EmailSenderController {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private MonitorServiceImpl monitorService;

    @Autowired
    private MonitoringMonitorRepository monitoringMonitorRepository;

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
    public ResponseEntity<String> notifySelectionResults(@RequestBody List<SelectionResultDTO> results) {
        List<MonitoringMonitor> seleccionadosParaEmail = new ArrayList<>(); 
        List<MonitoringMonitor> noSeleccionadosParaEmail = new ArrayList<>(); 
        List<MonitoringMonitor> actualizadosEnBD = new ArrayList<>(); 

        System.out.println("Controller: Recibidos " + results.size() + " resultados del frontend."); 

        for (SelectionResultDTO dto : results) {
            System.out.println("Controller: Procesando DTO: code=" + dto.getCode() + ", monitoringId=" + dto.getIdMonitoring() + ", estado=" + dto.getEstadoSeleccion());
            Optional<MonitoringMonitor> optionalRelation = monitoringMonitorRepository
                .findByMonitoringIdAndMonitorCode(dto.getIdMonitoring(), dto.getCode());

            if (optionalRelation.isPresent()) {
                MonitoringMonitor relacion = optionalRelation.get();
                String estadoPrevioEnBD = relacion.getEstadoSeleccion();
                String estadoNuevoDelDto = dto.getEstadoSeleccion();

                relacion.setEstadoSeleccion(estadoNuevoDelDto);
                actualizadosEnBD.add(relacion);

                if (estadoNuevoDelDto != null && "seleccionado".equalsIgnoreCase(estadoNuevoDelDto.trim())) {
                    if (estadoPrevioEnBD == null || !"seleccionado".equalsIgnoreCase(estadoPrevioEnBD.trim())) {
                        seleccionadosParaEmail.add(relacion);
                        System.out.println("Controller: Añadido a seleccionadosParaEmail (cambio detectado o previo null)");
                    } else {
                        System.out.println("Controller: Ya era seleccionado, no se añade a lista de email de seleccionados.");
                    }

                } else if (estadoNuevoDelDto != null && "no seleccionado".equalsIgnoreCase(estadoNuevoDelDto.trim())) {
                    
                    noSeleccionadosParaEmail.add(relacion);
                    System.out.println("Controller: Añadido a noSeleccionadosParaEmail");
                } else {
                    System.out.println("Controller: Estado DTO no reconocido o nulo: '" + estadoNuevoDelDto + "'");
                }
            } else {
                System.out.println("Controller: No se encontró relación para DTO: code=" + dto.getCode() + ", monitoringId=" + dto.getIdMonitoring());
            }
        }

        if (!actualizadosEnBD.isEmpty()) {
            monitoringMonitorRepository.saveAll(actualizadosEnBD);
            System.out.println("Controller: Guardados " + actualizadosEnBD.size() + " cambios en BD.");
        }
        
        System.out.println("Controller: Tamaño de seleccionadosParaEmail: " + seleccionadosParaEmail.size());
        if (!seleccionadosParaEmail.isEmpty()) {
            emailSenderService.sendToMonitors(seleccionadosParaEmail, true); 
        }

        System.out.println("Controller: Tamaño de noSeleccionadosParaEmail: " + noSeleccionadosParaEmail.size());
        if (!noSeleccionadosParaEmail.isEmpty()) {
            emailSenderService.sendToMonitors(noSeleccionadosParaEmail, false);
        }

        return ResponseEntity.ok("Proceso de selección finalizado. Notificaciones enviadas.");
    }


    // To try it, by GET
    //http://localhost:5433/send-basic-email?to=any@gmail.com&subject=Hello&body=Test Email
}
