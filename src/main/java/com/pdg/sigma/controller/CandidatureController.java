package com.pdg.sigma.controller;

import com.pdg.sigma.domain.Candidature;
import com.pdg.sigma.dto.CandidatureDTO;
import com.pdg.sigma.service.CandidatureServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
}
