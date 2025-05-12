package com.pdg.sigma.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.dto.CourseDTO;
import com.pdg.sigma.service.CourseServiceImpl;

//@CrossOrigin(origins = "https://pdg-sigma.vercel.app/")
@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
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

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<Course>> getCoursesByProgram(@PathVariable Long programId) {
        List<Course> courses = courseService.findByProgramId(programId);

        if (courses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(courses);
    }

    @GetMapping(value= "/getA")
    public ResponseEntity<?> getAll(){
        List<CourseDTO> list = courseService.findAll();
        return ResponseEntity.status(200).body(list);
    }
    
    @GetMapping(value= "/getCoursesByProfessor/{professorId}")
    public ResponseEntity<?> getCoursesByProfessor(@PathVariable String professorId){
        List<Course> list = courseService.getCoursesByProfessorId(professorId);
        return ResponseEntity.status(200).body(list);
    }




}
