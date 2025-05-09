package com.pdg.sigma.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pdg.sigma.service.ProfessorServiceImpl;

@CrossOrigin(origins = "https://pdg-sigma.vercel.app/")
@RequestMapping("/professor")
@RestController
public class ProfessorController {

    @Autowired
    private ProfessorServiceImpl professorService;

    @RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> profile(@PathVariable String id) {

        try {
            return ResponseEntity.ok(professorService.getProfile(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
