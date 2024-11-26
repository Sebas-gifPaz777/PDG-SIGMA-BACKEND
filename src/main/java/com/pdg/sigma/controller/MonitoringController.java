package com.pdg.sigma.controller;

import com.pdg.sigma.domain.Candidature;
import com.pdg.sigma.domain.Monitoring;
import com.pdg.sigma.dto.MonitoringDTO;
import com.pdg.sigma.service.MonitoringServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/monitoring")
@RestController
public class MonitoringController {

    @Autowired
    MonitoringServiceImpl monitoringService;

    @RequestMapping(value= "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createMonitoring(@RequestBody MonitoringDTO newMonitoring){
        try{
            Monitoring monitoring = monitoringService.save(newMonitoring);
            if(monitoringService.findById(monitoring.getId()).isPresent())
                return ResponseEntity.status(200).body("Se ha creado una monitoria");

            return ResponseEntity.status(400).body("No se pudo crear la monitoria");
        }catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }
}
