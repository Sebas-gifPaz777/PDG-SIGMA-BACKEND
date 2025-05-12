package com.pdg.sigma.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdg.sigma.domain.Attendance;
import com.pdg.sigma.service.AttendanceServiceImpl;

//@CrossOrigin(origins = "https://pdg-sigma.vercel.app/")
@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceServiceImpl attendanceService;
    
    @GetMapping("/getA") 
    public List<Attendance> getAllAttendances() {
        return attendanceService.findAll();
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<Attendance>> getAttendancesByActivity(@PathVariable Integer activityId) {
        List<Attendance> attendances = attendanceService.findByActivity(activityId);
        return ResponseEntity.ok(attendances != null ? attendances : Collections.emptyList());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAttendance(@RequestBody Attendance attendance) {
        try {
            if (attendance.getActivity() == null || attendance.getActivity().getId() == null) {
                return ResponseEntity.badRequest().body("La actividad no puede ser nula.");
            }
            if (attendance.getStudent() == null || attendance.getStudent().getCode() == null) {
                return ResponseEntity.badRequest().body("El estudiante no puede ser nulo.");
            }
            Attendance savedAttendance = attendanceService.save(attendance);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAttendance);

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: La asistencia ya existe.");
        }
    }

    @GetMapping("/check/{activityId}/{studentId}")
    public ResponseEntity<Boolean> checkAttendanceExists(
            @PathVariable Integer activityId,
            @PathVariable String studentId) {
        
        Optional<Attendance> existingAttendance = attendanceService.findByActivityAndStudent(activityId, studentId);
        return ResponseEntity.ok(existingAttendance.isPresent());
    }

    @DeleteMapping("/delete/{activityId}/{studentId}")
    public ResponseEntity<Void> deleteAttendance(
            @PathVariable Integer activityId,
            @PathVariable String studentId) {

        Optional<Attendance> existingAttendance = attendanceService.findByActivityAndStudent(activityId, studentId);
        
        if (existingAttendance.isPresent()) {
            attendanceService.delete(existingAttendance.get());
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
