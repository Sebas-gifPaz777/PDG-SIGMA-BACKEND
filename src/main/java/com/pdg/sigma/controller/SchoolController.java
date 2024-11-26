package com.pdg.sigma.controller;
import com.pdg.sigma.dto.SchoolDTO;
import com.pdg.sigma.service.SchoolServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/school")
@RestController
public class SchoolController {

    @Autowired
    private SchoolServiceImpl schoolService;

    @RequestMapping(value= "/getSchools", method = RequestMethod.GET)
    public ResponseEntity<?> getSchools(){
        List<SchoolDTO> list = schoolService.findAll();
        if(!list.isEmpty()){
            return ResponseEntity.status(200).body(list);
        }
        return ResponseEntity.status(400).body("No se encontraron facultades");
    }
}
