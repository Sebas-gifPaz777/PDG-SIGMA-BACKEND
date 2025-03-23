package com.pdg.sigma.controller;

import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.dto.MonitorDTO;
import com.pdg.sigma.service.MonitorServiceImpl;
import com.pdg.sigma.service.ProfessorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/professor")
@RestController
public class ProfessorController {

    @Autowired
    private ProfessorServiceImpl professorService;

    @RequestMapping(value = "profile/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> profile(@PathVariable String id) {

        try {
            return ResponseEntity.ok(professorService.getProfile(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
