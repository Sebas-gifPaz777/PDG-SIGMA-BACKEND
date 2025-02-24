package com.pdg.sigma.controller;

import com.pdg.sigma.domain.Activity;
import com.pdg.sigma.dto.ActivityDTO;
import com.pdg.sigma.dto.ActivityRequestDTO;
import com.pdg.sigma.service.ActivityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import com.pdg.sigma.dto.ActivityDTO;
//import com.pdg.sigma.service.ActivityServiceImpl;



@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/activity")
@RestController
public class ActivityController {

    @Autowired
    private ActivityServiceImpl activityService;

    @RequestMapping(value= "/findAll/{userId}/{role}", method = RequestMethod.GET)
    public ResponseEntity<?> getActivitiesPerUser(@PathVariable String userId, @PathVariable String role){
        try{
            List<ActivityDTO> list = activityService.findAll(userId, role);

            return ResponseEntity.status(200).body(list);

        }catch (Exception e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createActivity(@RequestBody ActivityRequestDTO requestDTO) {
        try {
            ActivityDTO activity = activityService.save(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(activity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteActivity(@PathVariable String id) {

        try {
            activityService.deleteById(Integer.parseInt(id));
            return ResponseEntity.ok("Actividad eliminada"); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateActivity(@RequestBody ActivityRequestDTO updatedActivity) {
        try {
            ActivityDTO activity = activityService.update(updatedActivity);
            return ResponseEntity.ok(activity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getActivityById(@PathVariable Integer id) {
        Optional<Activity> activity = activityService.findById(id);

        return activity.isPresent() 
            ? ResponseEntity.ok(new ActivityDTO(activity.get())) 
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activity not found");
    }


    @RequestMapping(value = "/getA", method = RequestMethod.GET)
    public ResponseEntity<?> getAllActivities() {
        try {
            List<ActivityDTO> dtos = activityService.findAll()
                .stream()
                .map(ActivityDTO::new)
                .collect(Collectors.toList());

            if (!dtos.isEmpty()) {
                return ResponseEntity.ok(dtos);
            }
            return ResponseEntity.status(400).body("No hay actividades en la lista");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }


    @RequestMapping(value= "/updateState", method = RequestMethod.PUT)
    public ResponseEntity<?> setActivityState(@RequestBody String id){
        try{
            activityService.updateState(id);
            return ResponseEntity.status(200).body("Estado cambiado");

        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }
}
