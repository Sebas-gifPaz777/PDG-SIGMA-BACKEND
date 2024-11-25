package com.pdg.sigma.controller;
import com.pdg.sigma.dto.ProgramDTO;
import com.pdg.sigma.service.ProgramServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/program")
@RestController
public class ProgramController {

    @Autowired
    private ProgramServiceImpl programService;

    @RequestMapping(value= "/getProgramsSchool", method = RequestMethod.GET)
    public ResponseEntity<?> getProgramsPerSchool(@RequestBody ProgramDTO program){

        List<ProgramDTO> list = programService.findBySchoolName(program);
        if(!list.isEmpty())
            return ResponseEntity.status(200).body(list);
        else
            return ResponseEntity.status(400).body("Programas no encontrados");
    }
}
