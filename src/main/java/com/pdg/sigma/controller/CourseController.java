package com.pdg.sigma.controller;
import com.pdg.sigma.dto.CourseDTO;
import com.pdg.sigma.service.CourseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/course")
@RestController
public class CourseController {

    @Autowired
    private CourseServiceImpl courseService;

    @RequestMapping(value= "/getCoursesProgram", method = RequestMethod.POST)
    public ResponseEntity<?> getCoursesPerProgram(@RequestBody CourseDTO course){
        List<CourseDTO> list = courseService.findByProgram(course);
        return ResponseEntity.status(200).body(list);


    }
}
