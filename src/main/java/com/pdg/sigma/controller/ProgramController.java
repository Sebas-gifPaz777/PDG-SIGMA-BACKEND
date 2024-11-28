package com.pdg.sigma.controller;
import com.pdg.sigma.dto.ProgramDTO;
import com.pdg.sigma.service.ProgramServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
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
