package com.pdg.sigma.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pdg.sigma.dto.ActivityDTO;
import com.pdg.sigma.service.ActivityServiceImpl;



@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/activity")
@RestController
public class ActivityController {

    @Autowired
    private ActivityServiceImpl activityService;

    @RequestMapping(value= "/findAll/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getActivitiesPerUser(@PathVariable String userId){
        try{
            List<ActivityDTO> list = activityService.findAll(userId);

            return ResponseEntity.status(200).body(list);

        }catch (Exception e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @RequestMapping(value= "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createActivity(@RequestBody ActivityDTO newActivity){
        try{
            ActivityDTO activity = activityService.save(newActivity);
            if(activityService.findById(activity.getId()).isPresent())
                return ResponseEntity.status(200).body("Se ha creado la actividad");

            return ResponseEntity.status(400).body("No se pudo crear la actividad");
        }catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
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

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateActivity(@RequestBody ActivityDTO updatedActivity) {
        try {
            ActivityDTO activity = activityService.update(updatedActivity);
            return ResponseEntity.status(200).body("Se ha actualizado la actividad correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping(value= "/{id}")
    public ResponseEntity<?> getActivityById(@PathVariable String id) {
        
        Optional<ActivityDTO> activity = activityService.findById(Integer.parseInt(id));

        if (activity.isPresent()) {
            return ResponseEntity.ok(activity.get()); 
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activity not found"); 
        }
    }

    @RequestMapping(value= "/getA", method = RequestMethod.GET)
    public ResponseEntity<?> getAllAct(){
        try{
            List<ActivityDTO> listActivity = activityService.findAll();
            if(!listActivity.isEmpty()){
                return ResponseEntity.status(200).body(listActivity);
            }

            return ResponseEntity.status(400).body("No hay acts en la lista");
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }


}
