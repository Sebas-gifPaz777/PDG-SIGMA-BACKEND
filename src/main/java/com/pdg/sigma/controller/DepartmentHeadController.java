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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pdg.sigma.domain.DepartmentHead;
import com.pdg.sigma.domain.HeadProgram;
import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.repository.HeadProgramRepository;
import com.pdg.sigma.service.DepartmentHeadService;

@RestController
@RequestMapping("/department-head")
//@CrossOrigin(origins = "https://pdg-sigma.vercel.app/")
//@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
public class DepartmentHeadController {

    @Autowired
    private DepartmentHeadService departmentHeadService;

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

    @GetMapping("/{id}/program")
    public ResponseEntity<List<HeadProgram>> getPrograms(@PathVariable Integer id) {
        List<HeadProgram> headProfessors = headProgramRepository.findByDepartmentHeadId(id.toString());
        if (headProfessors.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(headProfessors);
    }

    @GetMapping("/{id}/professors")
    public ResponseEntity<List<Professor>> getProfessorsByDepartmentHead(@PathVariable String id) {
        List<Professor> professors = departmentHeadService.getProfessorsByDepartmentHead(id);

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
