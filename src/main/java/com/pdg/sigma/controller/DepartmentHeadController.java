package com.pdg.sigma.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;

import com.pdg.sigma.domain.DepartmentHead;
import com.pdg.sigma.domain.HeadProfessor;
import com.pdg.sigma.repository.HeadProfessorRepository;
import com.pdg.sigma.service.DepartmentHeadService;

@RestController
@RequestMapping("/department-head")
@CrossOrigin(origins = "http://localhost:3000")
public class DepartmentHeadController {

    @Autowired
    private DepartmentHeadService departmentHeadService;

    @Autowired
    private HeadProfessorRepository headProfessorRepository;

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
        updatedDepartmentHead.setId(id); // Asegurar que se actualiza el correcto
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
    public ResponseEntity<List<HeadProfessor>> getProfessorsByDepartmentHead(@PathVariable Integer id) {
        List<HeadProfessor> headProfessors = headProfessorRepository.findByDepartmentHeadId(id);
        if (headProfessors.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(headProfessors);
    }
}
