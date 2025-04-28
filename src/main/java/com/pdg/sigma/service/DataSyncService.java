package com.pdg.sigma.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.CourseProfessor;
import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.dto.UpdateRequestDTO;
import com.pdg.sigma.repository.CourseProfessorRepository;
import com.pdg.sigma.repository.CourseRepository;
import com.pdg.sigma.repository.MonitorRepository;
import com.pdg.sigma.repository.MonitoringMonitorRepository;
import com.pdg.sigma.repository.MonitoringRepository;
import com.pdg.sigma.repository.ProfessorRepository;

@Service
public class DataSyncService {

    private final String EXTERNAL_API_BASE_URL = "https://api-banner.onrender.com/api";

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
            syncProfessors(professor.getId());
            String url = EXTERNAL_API_BASE_URL + "/courses/byProfessor/" + professor.getId();
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
                .toList();
        System.out.println("Cursos actuales asociados al profesor: " + currentCourses.stream().map(Course::getId).collect(java.util.stream.Collectors.toList()));
        System.out.println("Cursos obtenidos de la API: " + newCourses.stream().map(Course::getId).collect(java.util.stream.Collectors.toList()));
    
        monitoringRepository.findByProfessor(professor).forEach(monitoring -> {
            monitoringMonitorRepository.deleteByMonitoring(monitoring); 
        });
    
        List<Course> coursesNoLongerAssociated = currentCourses.stream()
                .filter(course -> newCourses.stream().noneMatch(newCourse -> newCourse.getId().equals(course.getId())))
                .toList();

        coursesNoLongerAssociated.forEach(course -> {
            System.out.println("Procesando curso que ya no está asociado: ID " + course.getId() + ", Nombre: " + course.getName());
            monitoringRepository.findByCourse(course).ifPresent(monitoring -> {
                System.out.println("  Encontrada monitoría ID: " + monitoring.getId() + " para el curso ID: " + course.getId());
                monitoringMonitorRepository.deleteByMonitoring(monitoring); // Borra los monitores del curso
                System.out.println("  Monitores de la monitoría ID: " + monitoring.getId() + " eliminados.");
                monitoringRepository.delete(monitoring); // Borra la monitoría del curso
                System.out.println("  Monitoría ID: " + monitoring.getId() + " eliminada para el curso ID: " + course.getId());
            });
            courseProfessorRepository.deleteByCourseAndProfessor(course, professor);
            System.out.println("  Relación CourseProfessor para el curso ID: " + course.getId() + " y el profesor ID: " + professor.getId() + " eliminada.");
        });
    
        for (Course newCourse : newCourses) {
            Course courseToAdd = courseRepository.findById(newCourse.getId()).orElseGet(() -> {
                Course newCreatedCourse = new Course();
                newCreatedCourse.setId(newCourse.getId());
                newCreatedCourse.setName(newCourse.getName());
                newCreatedCourse.setProgram(newCourse.getProgram());
                Course savedCourse = courseRepository.save(newCreatedCourse);
                System.out.println("  Curso ID: " + savedCourse.getId() + " creado y guardado.");
                return savedCourse;
            });
    
            boolean alreadyAssociated = currentCourses.stream().anyMatch(course -> course.getId().equals(courseToAdd.getId()));
            if (!alreadyAssociated) {
                CourseProfessor newRelation = new CourseProfessor();
                newRelation.setCourse(courseToAdd);
                newRelation.setProfessor(professor);
                courseProfessorRepository.save(newRelation);
                System.out.println("  Relación CourseProfessor creada para el curso ID: " + courseToAdd.getId() + " y el profesor ID: " + professor.getId() + ".");
            } 
        }
    }
    
    private Course findCourseById(List<Course> courses, Long courseId) {
        return courses.stream().filter(course -> course.getId().equals(courseId)).findFirst().orElse(null);
    }

    public void syncProfessors(String professorId) {
        if (professorId == null || professorId.isEmpty()) {
            return ;
        }

        String url = EXTERNAL_API_BASE_URL + "/professors/" + professorId;
        Professor externalProfessor = restTemplate.getForObject(url, Professor.class);
          
        if (externalProfessor != null) {
            
            if (!professorRepository.existsById(externalProfessor.getId())) {
                professorRepository.save(externalProfessor);
            } else {
                professorRepository.save(externalProfessor); // Actualiza
            }
        } 
    
    }
}

