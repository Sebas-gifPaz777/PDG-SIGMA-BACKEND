package com.pdg.sigma.controller;

import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.dto.MonitorDTO;
import com.pdg.sigma.service.MonitorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@CrossOrigin(origins = "https://pdg-sigma.vercel.app/")
//@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
@CrossOrigin(origins = {"http://200.3.193.229:3000", "https://pdg-sigma.vercel.app/"})

@RequestMapping("/monitor")
@RestController
public class MonitorController {

    @Autowired
    private MonitorServiceImpl monitorService;

    @RequestMapping(value= "/create", method =RequestMethod.POST)
    public ResponseEntity<?>createMonitor(@RequestBody MonitorDTO newCandidature){
        try{
            Monitor mon = monitorService.saveNew(newCandidature);
            return ResponseEntity.status(200).body("Se ha creado una postulación");
        }catch(Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }

    @RequestMapping(value= "/getA", method = RequestMethod.GET)
    public ResponseEntity<?> getAllMonitor(){
        try{
            List<MonitorDTO> listMonitor = monitorService.findAllNew();
            if(!listMonitor.isEmpty()){
                return ResponseEntity.status(200).body(listMonitor);
            }

            return ResponseEntity.status(404).body("No hay postulantes en la lista");
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCandidature(@PathVariable String id) {//Cambiar nombre

        try {
            monitorService.deleteById(id);
            return ResponseEntity.ok("Candidature deleted successfully"); //Cambiar respuesta
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @RequestMapping(value = "profile/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> profile(@PathVariable String id) {//Cambiar nombre

        try {
            return ResponseEntity.ok(monitorService.getProfile(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /*@RequestMapping(value= "/getPerCourse/{course}", method = RequestMethod.GET)
    public ResponseEntity<?> getMonitorPerCourse(@PathVariable String course){
        try{
            List<Monitor> listMonitor = monitorService.findPerCourse(course);
            return ResponseEntity.status(200).body(listMonitor);

        }catch (Exception e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }*/


}
