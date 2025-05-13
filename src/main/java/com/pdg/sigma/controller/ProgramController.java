package com.pdg.sigma.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pdg.sigma.dto.ProgramDTO;
import com.pdg.sigma.service.ProgramServiceImpl;

//@CrossOrigin(origins = "https://pdg-sigma.vercel.app/")
//@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})

@RequestMapping("/program")
@RestController
public class ProgramController {

    @Autowired
    private ProgramServiceImpl programService;

    @RequestMapping(value= "/getProgramsSchool", method = RequestMethod.POST)
    public ResponseEntity<?> getProgramsPerSchool(@RequestBody ProgramDTO program){
        List<ProgramDTO> list = programService.findBySchoolName(program);
        return ResponseEntity.status(200).body(list);
    }
}
