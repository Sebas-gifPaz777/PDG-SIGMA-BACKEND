package com.pdg.sigma.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pdg.sigma.domain.Candidature;
import com.pdg.sigma.dto.CandidatureDTO;
import com.pdg.sigma.service.CandidatureServiceImpl;


@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/candidature")
@RestController
public class CandidatureController {

    @Autowired
    private CandidatureServiceImpl candidatureService;

    @RequestMapping(value= "/create", method =RequestMethod.POST)
    public ResponseEntity<?>createCandiduture(@RequestBody CandidatureDTO newCandidature){
        try{
            Candidature can = candidatureService.saveNew(newCandidature);
            return ResponseEntity.status(200).body("Se ha creado una postulación");
        }catch(Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }

    @RequestMapping(value= "/getA", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCandidature(){
        try{
            List<Candidature> listCandidature = candidatureService.findAll();
            if(!listCandidature.isEmpty()){
                return ResponseEntity.status(200).body(listCandidature);
            }

            return ResponseEntity.status(400).body("No hay postulantes en la lista");
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCandidature(@PathVariable String id) {
        
        try {
            candidatureService.deleteById(id);
            return ResponseEntity.ok("Candidature deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
