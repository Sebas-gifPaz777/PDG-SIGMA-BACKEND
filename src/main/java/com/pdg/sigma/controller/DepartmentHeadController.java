package com.pdg.sigma.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.DepartmentHead;
import com.pdg.sigma.domain.HeadProgram;
import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.repository.CourseProfessorRepository;
import com.pdg.sigma.repository.CourseRepository;
import com.pdg.sigma.repository.HeadProgramRepository;
import com.pdg.sigma.service.DepartmentHeadServiceImpl;

@RestController
@RequestMapping("/department-head")
@CrossOrigin(origins = "http://localhost:3000")
public class DepartmentHeadController {

    @Autowired
    private DepartmentHeadServiceImpl departmentHeadService;

    @Autowired
    private HeadProgramRepository headProgramRepository;

    @Autowired
    private CourseProfessorRepository courseProfessorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/getA")
    public List<DepartmentHead> getAllDepartmentHeads() {
        return departmentHeadService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentHead> getDepartmentHeadById(@PathVariable Integer id) {
        Optional<DepartmentHead> departmentHead = departmentHeadService.findById(id);
        return departmentHead.map(ResponseEntity::ok)
                             .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<DepartmentHead> createDepartmentHead(@RequestBody DepartmentHead departmentHead) throws Exception {
        return ResponseEntity.ok(departmentHeadService.save(departmentHead));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DepartmentHead> updateDepartmentHead(@PathVariable Integer id, @RequestBody DepartmentHead updatedDepartmentHead) throws Exception {
        if (!departmentHeadService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        updatedDepartmentHead.setId(id.toString()); // Asegurar que se actualiza el correcto
        return ResponseEntity.ok(departmentHeadService.save(updatedDepartmentHead));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDepartmentHead(@PathVariable Integer id) throws Exception {
        if (!departmentHeadService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        departmentHeadService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/program")
    public ResponseEntity<List<HeadProgram>> getPrograms(@PathVariable Integer id) {
        List<HeadProgram> headProfessors = headProgramRepository.findByDepartmentHeadId(id.toString());
        if (headProfessors.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(headProfessors);
    }

    @GetMapping("/{id}/professors")
    public ResponseEntity<List<Professor>> getProfessorsByDepartmentHead(@PathVariable Integer id) {
        
        List<HeadProgram> headPrograms = headProgramRepository.findByDepartmentHeadId(id.toString());

        if (headPrograms.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Long> programIds = headPrograms.stream()
                .map(headProgram -> headProgram.getProgram().getId()) 
                .collect(Collectors.toList());

        List<Course> courses = courseRepository.findByProgramIdIn(programIds);

        if (courses.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList()); 
        }

        List<Long> courseIds = courses.stream()
                .map(Course::getId)
                .collect(Collectors.toList());

        List<Professor> professors = courseProfessorRepository.findProfessorsByCourseIds(courseIds);

        return ResponseEntity.ok(professors);
    }

    @RequestMapping(value= "/profile/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCoursesPerProgram(@PathVariable String id){

        try {
            return ResponseEntity.ok(departmentHeadService.getProfile(id));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }
}
