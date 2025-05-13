package com.pdg.sigma.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pdg.sigma.dto.SchoolDTO;
import com.pdg.sigma.service.SchoolServiceImpl;

//@CrossOrigin(origins = "https://pdg-sigma.vercel.app/")
//@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})

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
