package com.pdg.sigma.controller;


import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.dto.ActivityDTO;
import com.pdg.sigma.dto.AuthDTO;
import com.pdg.sigma.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/activity")
@RestController
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @RequestMapping(value= "/findAll/{userId}/{role}", method = RequestMethod.GET)
    public ResponseEntity<?> getActivitiesPerUser(@PathVariable String userId, @PathVariable String role){
        try{
            List<ActivityDTO> list = activityService.findAll(userId, role);

            return ResponseEntity.status(200).body(list);

        }catch (Exception e){
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }
}
