package com.pdg.sigma.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdg.sigma.domain.Student;
import com.pdg.sigma.domain.StudentCourse;
import com.pdg.sigma.service.StudentServiceImpl;

//@CrossOrigin(origins = "https://pdg-sigma.vercel.app/")
@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentServiceImpl studentService;
    
    @GetMapping("/getA") 
    public List<Student> getAllStudents() {
        return studentService.findAll();
    }

    @GetMapping("/{code}")  
    public ResponseEntity<Student> getStudentByCode(@PathVariable String code) {
        Optional<Student> student = studentService.findById(code);

        return student.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<StudentCourse>> getStudentsByCourse(@PathVariable Integer courseId) {
        List<StudentCourse> students = studentService.getStudentsByCourse(courseId);
        return ResponseEntity.ok(students);
    }
}
