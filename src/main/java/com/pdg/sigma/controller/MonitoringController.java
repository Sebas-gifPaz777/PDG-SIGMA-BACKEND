package com.pdg.sigma.controller;

import com.pdg.sigma.domain.Monitoring;
import com.pdg.sigma.dto.MonitoringDTO;
import com.pdg.sigma.service.MonitoringServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping(value= "/getA", method = RequestMethod.GET)
    public ResponseEntity<?> getAllMonitoring(){
        try{
            List<Monitoring> listMonitoring = monitoringService.findAll();
            if(!listMonitoring.isEmpty()){
                return ResponseEntity.status(200).body(listMonitoring);
            }

            return ResponseEntity.status(400).body("No hay monitorias en la lista");
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @RequestMapping(value= "/findByFaculty", method = RequestMethod.POST)
    public ResponseEntity<?> getAllMonitoringPerSchool(@RequestBody MonitoringDTO monitoringDTO){
        try{
            System.out.println(monitoringDTO);
            List<Monitoring> listMonitoring = monitoringService.findBySchool(monitoringDTO);
            if(!listMonitoring.isEmpty()){
                return ResponseEntity.status(200).body(listMonitoring);
            }

            return ResponseEntity.status(400).body("No hay monitorias en la lista");
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @RequestMapping(value= "/findByProgram", method = RequestMethod.POST)
    public ResponseEntity<?> getAllMonitoringPerProgram(@RequestBody MonitoringDTO monitoringDTO){
        try{
            List<Monitoring> listMonitoring = monitoringService.findByProgram(monitoringDTO);
            if(!listMonitoring.isEmpty()){
                return ResponseEntity.status(200).body(listMonitoring);
            }

            return ResponseEntity.status(400).body("No hay monitorias en la lista");
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @RequestMapping(value= "/findByCourse", method = RequestMethod.POST)
    public ResponseEntity<?> getAllMonitoringPerCourse(@RequestBody MonitoringDTO monitoringDTO){
        try{
            List<Monitoring> listMonitoring = monitoringService.findByCourse(monitoringDTO);
            if(!listMonitoring.isEmpty()){
                return ResponseEntity.status(200).body(listMonitoring);
            }

            return ResponseEntity.status(400).body("No hay monitorias en la lista");
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

   @RequestMapping(value= "/createAll/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> createMultipleMonitoring(@RequestParam("file") MultipartFile file, @PathVariable String id){
       try {
            return ResponseEntity.status(200).body(monitoringService.processListMonitor(file,id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al procesar el archivo: " + e.getMessage());
        }

    }

    @RequestMapping(value= "/profile/{id}/{role}", method = RequestMethod.GET)
    public ResponseEntity<?> monitoringToProfile(@PathVariable String id, @PathVariable String role){
        try {
            if(role.equalsIgnoreCase("professor"))
                return ResponseEntity.status(200).body(monitoringService.getByProfessor(id));
            else
                return ResponseEntity.status(200).body(monitoringService.getByMonitor(id));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }

}
