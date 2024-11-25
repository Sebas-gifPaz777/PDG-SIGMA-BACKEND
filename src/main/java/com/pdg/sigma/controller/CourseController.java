package com.pdg.sigma.controller;
import com.pdg.sigma.dto.CourseDTO;
import com.pdg.sigma.service.CourseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/course")
@RestController
public class CourseController {

    @Autowired
    private CourseServiceImpl courseService;

    @RequestMapping(value= "/getCoursesProgram", method = RequestMethod.GET)
    public ResponseEntity<?> getCoursesPerProgram(@RequestBody CourseDTO course){
        List<CourseDTO> list = courseService.findByProgram(course);
        if(!list.isEmpty()){
            return ResponseEntity.status(200).body(list);
        }
        return ResponseEntity.status(400).body("No se encontro un Curso");
    }
}
