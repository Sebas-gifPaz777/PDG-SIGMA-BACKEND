package com.pdg.sigma.controller;

import com.pdg.sigma.domain.Activity;
import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.HeadProgram;
import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.dto.ActivityDTO;
import com.pdg.sigma.dto.ActivityRequestDTO;
import com.pdg.sigma.dto.NewAcitivityRequestDTO;
import com.pdg.sigma.service.ActivityServiceImpl;
import com.pdg.sigma.service.DepartmentHeadServiceImpl;
import com.pdg.sigma.service.CourseServiceImpl;

import com.pdg.sigma.repository.CourseProfessorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
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

    @Autowired
    private CourseServiceImpl courseService;
    
    @Autowired
    private CourseProfessorRepository courseProfessorRepository;
    
    @Autowired
    private DepartmentHeadServiceImpl departmentHeadService;

    // @RequestMapping(value= "/findAll/{userId}/{role}", method = RequestMethod.GET)
    // public ResponseEntity<?> getActivitiesPerUser(@PathVariable String userId, @PathVariable String role) {
    //     try {
    //         List<ActivityDTO> activities = new ArrayList<>();

    //         if (role.equals("jfedpto")) {
    //             List<Professor> professors = departmentHeadService.getProfessorsByDepartmentHead(Integer.valueOf(userId));

    //             for (Professor professor : professors) {
    //                 List<ActivityDTO> professorActivities = activityService.findAll(professor.getId(), "professor");
    //                 activities.addAll(professorActivities);
    //             }
    //         } else { // si no es jefe
    //             activities = activityService.findAll(userId, role);
    //         }

    //         return ResponseEntity.status(200).body(activities);

    //     } catch (Exception e) {
    //         return ResponseEntity.status(404).body(e.getMessage());
    //     }
    // }

    @RequestMapping(value = "/findAll/{userId}/{role}", method = RequestMethod.GET)
    public ResponseEntity<?> getActivitiesPerUser(@PathVariable String userId, @PathVariable String role) {
        try {
            List<ActivityDTO> activities = new ArrayList<>();

            if (role.equals("jfedpto")) {
                List<HeadProgram> headPrograms = departmentHeadService.getProgramsByDepartmentHead(userId);

                if (headPrograms.isEmpty()) {
                    return ResponseEntity.ok(Collections.emptyList());
                }

                List<Long> programIds = headPrograms.stream()
                        .map(headProgram -> headProgram.getProgram().getId())
                        .collect(Collectors.toList());

                List<Course> courses = courseService.findByProgramIds(programIds);

                if (courses.isEmpty()) {
                    return ResponseEntity.ok(Collections.emptyList());
                }

                List<Long> courseIds = courses.stream()
                        .map(Course::getId)
                        .collect(Collectors.toList());

                // profesores 
                List<Professor> professors = courseProfessorRepository.findProfessorsByCourseIds(courseIds);

                if (professors.isEmpty()) {
                    return ResponseEntity.ok(Collections.emptyList());
                }

                for (Professor professor : professors) {
                    List<ActivityDTO> professorActivities = activityService.findAll(professor.getId(), "professor");
                    // Filtrar actividades solo de los cursos del programa
                    List<ActivityDTO> filteredActivities = professorActivities.stream()
                    .filter(activity -> courseIds.contains(activity.getMonitoring().getCourse().getId()))
                    .collect(Collectors.toList());

                    activities.addAll(filteredActivities);
                }
            } else {
                activities = activityService.findAll(userId, role);
            }

            return ResponseEntity.status(200).body(activities);

        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createActivity(@RequestBody NewAcitivityRequestDTO requestDTO) {
        try {
            ActivityDTO activity = activityService.save(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(activity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteActivity(@PathVariable Integer id) {
        try {
            activityService.deleteById(id);
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
            e.printStackTrace();
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
