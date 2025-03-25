package com.pdg.sigma.controller;

import java.util.List;
import java.util.Optional;

import com.pdg.sigma.domain.HeadProgram;
import com.pdg.sigma.service.DepartmentHeadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pdg.sigma.domain.DepartmentHead;
import com.pdg.sigma.repository.HeadProgramRepository;

@RestController
@RequestMapping("/department-head")
@CrossOrigin(origins = "http://localhost:3000")
public class DepartmentHeadController {

    @Autowired
    private DepartmentHeadServiceImpl departmentHeadService;

    @Autowired
    private HeadProgramRepository headProgramRepository;

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

    @GetMapping("/{id}/professors")
    public ResponseEntity<List<HeadProgram>> getProfessorsByDepartmentHead(@PathVariable Integer id) {
        List<HeadProgram> headProfessors = headProgramRepository.findByDepartmentHeadId(id.toString());
        if (headProfessors.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(headProfessors);
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
