package com.pdg.sigma.controller;

import com.pdg.sigma.domain.Candidature;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/candidature")
@RestController
public class CandidatureController {

    @RequestMapping(value= "/create", method =RequestMethod.POST)
    public ResponseEntity<?>createCandiduture(@RequestBody Candidature newCandidture){

    }
}
