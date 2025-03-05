package com.pdg.sigma.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdg.sigma.domain.Attendance;
import com.pdg.sigma.service.AttendanceServiceImpl;

@CrossOrigin(origins = "http://localhost:3000")
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

}
