package com.pdg.sigma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.pdg.sigma.repository.MonitorRepository;
import com.pdg.sigma.repository.ProfessorRepository;
import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.CourseProfessor;
import com.pdg.sigma.domain.Monitoring;
import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.dto.UpdateRequestDTO;
import com.pdg.sigma.repository.CourseProfessorRepository;
import com.pdg.sigma.repository.CourseRepository;
import com.pdg.sigma.repository.MonitoringMonitorRepository;
import com.pdg.sigma.repository.MonitoringRepository;

@Service
public class DataSyncService {

    private final ProfessorRepository professorRepository;
    private final CourseProfessorRepository courseProfessorRepository;
    private final MonitorRepository monitorRepository;
    private final MonitoringRepository monitoringRepository;
    private final MonitoringMonitorRepository monitoringMonitorRepository;
    private final CourseRepository courseRepository;
    private final RestTemplate restTemplate;
    private final DepartmentHeadServiceImpl departmentHeadService;

    @Autowired
    public DataSyncService(ProfessorRepository professorRepository, 
                           CourseProfessorRepository courseProfessorRepository, 
                           MonitorRepository monitorRepository,
                           MonitoringRepository monitoringRepository,
                           MonitoringMonitorRepository monitoringMonitorRepository,
                           CourseRepository courseRepository,
                           RestTemplate restTemplate,
                           DepartmentHeadServiceImpl departmentHeadService) {
        this.professorRepository = professorRepository;
        this.courseProfessorRepository = courseProfessorRepository;
        this.monitorRepository = monitorRepository;
        this.monitoringRepository = monitoringRepository;
        this.monitoringMonitorRepository = monitoringMonitorRepository;
        this.courseRepository = courseRepository;
        this.restTemplate = restTemplate;
        this.departmentHeadService = departmentHeadService;
    }

    @Transactional
    public String syncData(UpdateRequestDTO request) {

        List<Professor> professors;

        if (request.getProfessorId() != null) {
            Optional<Professor> professorOpt = professorRepository.findById(request.getProfessorId());
            if (professorOpt.isEmpty()) {
                throw new RuntimeException("El profesor con ID " + request.getProfessorId() + " no existe.");
            }
            professors = List.of(professorOpt.get());
        } else if (request.getDepartmentHeadId() != null) {
            professors = departmentHeadService.getProfessorsByDepartmentHead(request.getDepartmentHeadId());
        } else {
            // Buscar todos
            professors = professorRepository.findAll();
        }
        
        for (Professor professor : professors) {
            String url = "http://localhost:5431/api/courses/byProfessor/" + professor.getId();
            Course[] newCoursesArray = restTemplate.getForObject(url, Course[].class);
            if (newCoursesArray == null) continue;
            List<Course> newCourses = List.of(newCoursesArray);
            
            List<CourseProfessor> currentRelations = courseProfessorRepository.findByProfessor(professor);
            if ("sameSemester".equals(request.getUpdateType())) {
                updateSameSemester(professor, newCourses, currentRelations, request.isRemoveMonitors()); //Funcionando bien para professor y jfepdto
            } else if ("newSemester".equals(request.getUpdateType())) {
                updateNewSemester(professor, newCourses, currentRelations);
            }
        }

        return "Actualización completada con éxito.";
    }

    private void updateSameSemester(Professor professor, List<Course> newCourses, 
                                List<CourseProfessor> currentRelations, boolean removeMonitors) {
        for (CourseProfessor relation : currentRelations) {
            Course existingCourse = relation.getCourse();
            Course updatedCourse = findCourseById(newCourses, existingCourse.getId());
            

            if (updatedCourse != null && !existingCourse.getName().equals(updatedCourse.getName())) {
                existingCourse.setName(updatedCourse.getName());
                courseRepository.save(existingCourse); 

                if (removeMonitors) {
                    monitoringRepository.findByCourse(existingCourse).ifPresent(monitoring -> {
                        // Eliminar todas las relaciones
                        monitoringMonitorRepository.deleteByMonitoring(monitoring);
                    });
                }
            }
        }
    }

    private void updateNewSemester(Professor professor, List<Course> newCourses, List<CourseProfessor> currentRelations) {
        List<Course> currentCourses = currentRelations.stream()
            .map(CourseProfessor::getCourse)
            .collect(Collectors.toList());
    
        // Save names
        for (CourseProfessor relation : currentRelations) {
            Course existingCourse = relation.getCourse();
            Course updatedCourse = findCourseById(newCourses, existingCourse.getId());
    
            if (updatedCourse != null && !existingCourse.getName().equals(updatedCourse.getName())) {
                existingCourse.setName(updatedCourse.getName());
                courseRepository.save(existingCourse);
            }
        }
    
        // Crear relaciones nuevas, db
        for (Course newCourse : newCourses) {
            if (currentCourses.stream().noneMatch(course -> course.getId().equals(newCourse.getId()))) {
                // Verificar si el curso ya existe en la BD
                Course existingCourse = courseRepository.findById(newCourse.getId()).orElse(null);
                if (existingCourse == null) {
                    existingCourse = courseRepository.save(newCourse); // Guardar si no existe
                }
    
                CourseProfessor newRelation = new CourseProfessor();
                newRelation.setProfessor(professor);
                newRelation.setCourse(existingCourse);
                courseProfessorRepository.save(newRelation);
            }
        }
    
         // Eliminar TODAS las relaciones monitoring-monitor
         List<Monitoring> monitorings = monitoringRepository.findByProfessor(professor);
         
         for (Monitoring monitoring : monitorings) {
            //  System.out.println("Eliminando monitores de la monitoría ID: " + monitoring.getId());
             monitoringMonitorRepository.deleteByMonitoring(monitoring);
         }
         
    
        // Eliminar relaciones de CourseProfessor que ya no están en newCourses
        for (CourseProfessor relation : currentRelations) {
            if (newCourses.stream().noneMatch(course -> course.getId().equals(relation.getCourse().getId()))) {
                courseProfessorRepository.delete(relation);
            }
        }
    }
    
    


    private Course findCourseById(List<Course> courses, Long courseId) {
        return courses.stream().filter(course -> course.getId().equals(courseId)).findFirst().orElse(null);
    }
}

