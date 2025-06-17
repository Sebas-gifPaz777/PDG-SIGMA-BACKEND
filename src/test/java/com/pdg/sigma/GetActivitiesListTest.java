package com.pdg.sigma;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.pdg.sigma.domain.Activity;
import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.HeadProgram;
import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.domain.Monitoring;
import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.domain.Program;
import com.pdg.sigma.domain.StateActivity;
import com.pdg.sigma.repository.ActivityRepository;
import com.pdg.sigma.repository.CourseProfessorRepository;
import com.pdg.sigma.repository.CourseRepository;
import com.pdg.sigma.repository.MonitorRepository;
import com.pdg.sigma.repository.MonitoringRepository;
import com.pdg.sigma.repository.ProfessorRepository;
import com.pdg.sigma.repository.ProspectRepository;
import com.pdg.sigma.service.DepartmentHeadServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class GetActivitiesListTest {

    @Autowired
    private MockMvc mockMvc;

    // --- Repositorios y Servicios Mockeados ---
    // NO mockeamos ActivityService ni CourseServiceImpl para probar su lógica.
    @MockBean private ActivityRepository activityRepository;
    @MockBean private ProfessorRepository professorRepository;
    @MockBean private MonitorRepository monitorRepository;
    @MockBean private ProspectRepository prospectRepository;
    @MockBean private CourseRepository courseRepository;
    @MockBean private CourseProfessorRepository courseProfessorRepository;
    @MockBean private DepartmentHeadServiceImpl departmentHeadService;
    // Mockeamos MonitoringRepository ya que es una dependencia para crear objetos Activity
    @MockBean private MonitoringRepository monitoringRepository;


    // --- Objetos de Prueba ---
    private Professor mockProfessor;
    private Monitor mockMonitor;
    private Course mockCourse;
    private Monitoring mockMonitoring;
    private Activity mockActivityAssignedToProfessor;
    private Activity mockActivityCreatedByProfessor;

    @BeforeEach
    void setUp() {
        // Configuración de objetos comunes para las pruebas
        mockProfessor = new Professor();
        mockProfessor.setId("prof123");
        mockProfessor.setName("Profesor Mock");

        mockMonitor = new Monitor();
        mockMonitor.setIdMonitor("monitor456");
        mockMonitor.setName("Monitor");
        mockMonitor.setLastName("Mock");
        mockMonitor.setCode("M001");

        mockCourse = new Course();
        mockCourse.setId(1L);
        mockCourse.setName("Cálculo I");

        mockMonitoring = new Monitoring();
        mockMonitoring.setId(10L);
        mockMonitoring.setCourse(mockCourse);

        mockActivityCreatedByProfessor = new Activity();
        mockActivityCreatedByProfessor.setId(1);
        mockActivityCreatedByProfessor.setName("Actividad Creada por Profesor");
        mockActivityCreatedByProfessor.setRoleCreator("P");
        mockActivityCreatedByProfessor.setRoleResponsable("M");
        mockActivityCreatedByProfessor.setProfessor(mockProfessor);
        mockActivityCreatedByProfessor.setMonitor(mockMonitor);
        mockActivityCreatedByProfessor.setMonitoring(mockMonitoring);
        mockActivityCreatedByProfessor.setState(StateActivity.PENDIENTE);
        mockActivityCreatedByProfessor.setFinish(new java.util.Date());


        mockActivityAssignedToProfessor = new Activity();
        mockActivityAssignedToProfessor.setId(2);
        mockActivityAssignedToProfessor.setName("Actividad Asignada a Profesor");
        mockActivityAssignedToProfessor.setRoleCreator("M");
        mockActivityAssignedToProfessor.setRoleResponsable("P");
        mockActivityAssignedToProfessor.setProfessor(mockProfessor);
        mockActivityAssignedToProfessor.setMonitor(mockMonitor);
        mockActivityAssignedToProfessor.setMonitoring(mockMonitoring);
        mockActivityAssignedToProfessor.setState(StateActivity.COMPLETADO);
        mockActivityAssignedToProfessor.setFinish(new java.util.Date()); 
        // mockActivityAssignedToProfessor.setState(StateActivity.PENDIENTE);
    }

    @Test
    @WithMockUser
    public void getActivitiesPerUser_forProfessor_Success() throws Exception {
        // Arrange: Configurar mocks para que el servicio de profesor funcione
        when(professorRepository.findById("prof123")).thenReturn(Optional.of(mockProfessor));
        when(activityRepository.findByProfessorAndRoleCreator(mockProfessor, "P")).thenReturn(List.of(mockActivityCreatedByProfessor));
        when(activityRepository.findByProfessorAndRoleResponsable(mockProfessor, "P")).thenReturn(List.of(mockActivityAssignedToProfessor));

        // Act & Assert
        mockMvc.perform(get("/activity/findAll/{userId}/{role}", "prof123", "professor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Actividad Creada por Profesor"))
                .andExpect(jsonPath("$[0].type").value("C")) // Creada
                .andExpect(jsonPath("$[1].name").value("Actividad Asignada a Profesor"))
                .andExpect(jsonPath("$[1].type").value("A")); // Asignada
    }

    @Test
    @WithMockUser
    public void getActivitiesPerUser_forDepartmentHead_Success() throws Exception {
        // Arrange: Escenario complejo para el Jefe de Departamento (jfedpto)
        String deptHeadId = "jefeDpto1";
        
        // 1. El jefe de departamento tiene programas a su cargo
        Program programSistemas = new Program();
        programSistemas.setId(1L);
        HeadProgram headProgram = new HeadProgram();
        headProgram.setProgram(programSistemas);
        when(departmentHeadService.getProgramsByDepartmentHead(deptHeadId)).thenReturn(List.of(headProgram));
        
        // 2. Esos programas tienen cursos
        Course courseSistemas = new Course();
        courseSistemas.setId(1L);
        courseSistemas.setName("Estructuras de Datos");
        courseSistemas.setProgram(programSistemas);
        when(courseRepository.findByProgramIdIn(List.of(1L))).thenReturn(List.of(courseSistemas));
        
        // 3. Esos cursos tienen profesores asignados
        Professor profSistemas = new Professor();
        profSistemas.setId("profSistemas");
        profSistemas.setName("Dr. Codenberg");
        when(courseProfessorRepository.findProfessorsByCourseIds(List.of(1L))).thenReturn(List.of(profSistemas));

        // 4. Esos profesores tienen actividades
        Monitoring monitoringSistemas = new Monitoring();
        monitoringSistemas.setId(20L);
        monitoringSistemas.setCourse(courseSistemas); // Actividad pertenece al curso correcto

        Activity activitySistemas = new Activity();
        activitySistemas.setId(101);
        activitySistemas.setName("Tarea de Listas Enlazadas");
        activitySistemas.setMonitoring(monitoringSistemas);
        activitySistemas.setProfessor(profSistemas);
        activitySistemas.setRoleCreator("P");
        activitySistemas.setRoleResponsable("P");
        activitySistemas.setState(StateActivity.PENDIENTE);

        // Mocks para la llamada interna a activityService.findAll para el profesor encontrado
        when(professorRepository.findById("profSistemas")).thenReturn(Optional.of(profSistemas));
        when(activityRepository.findByProfessorAndRoleCreator(profSistemas, "P")).thenReturn(List.of(activitySistemas));
        when(activityRepository.findByProfessorAndRoleResponsable(profSistemas, "P")).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/activity/findAll/{userId}/{role}", deptHeadId, "jfedpto"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name").value("Tarea de Listas Enlazadas"))
            .andExpect(jsonPath("$[0].course").value("Estructuras de Datos"));
    }

    @Test
    @WithMockUser
    public void getActivitiesPerUser_NotFound() throws Exception {
        String userId = "professorWithNoActivities";
        String role = "professor";

        Professor professorWithoutActivities = new Professor();
        professorWithoutActivities.setId(userId);
        when(professorRepository.findById(userId)).thenReturn(Optional.of(professorWithoutActivities));

        when(activityRepository.findByProfessorAndRoleCreator(professorWithoutActivities, "P"))
                .thenReturn(Collections.emptyList());
        when(activityRepository.findByProfessorAndRoleResponsable(professorWithoutActivities, "P"))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/activity/findAll/{userId}/{role}", userId, role))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No actividades asignadas o creadas"));
    }

    // Este es el test que está fallando.
    @Test
    @WithMockUser
    public void getActivitiesPerUser_forDepartmentHead_WithNoActivities() throws Exception {
        
        String deptHeadId = "jefeDpto2";
        
        Program program = new Program();
        program.setId(2L);
        HeadProgram headProgram = new HeadProgram();
        headProgram.setProgram(program);
        when(departmentHeadService.getProgramsByDepartmentHead(deptHeadId)).thenReturn(List.of(headProgram));
        
        Course course = new Course();
        course.setId(2L);
        course.setProgram(program);
        when(courseRepository.findByProgramIdIn(List.of(2L))).thenReturn(List.of(course));
        
        Professor professor = new Professor();
        professor.setId("profSinActividades");
        when(courseProfessorRepository.findProfessorsByCourseIds(List.of(2L))).thenReturn(List.of(professor));

        when(professorRepository.findById("profSinActividades")).thenReturn(Optional.of(professor));
        when(activityRepository.findByProfessorAndRoleCreator(any(), any())).thenReturn(Collections.emptyList());
        when(activityRepository.findByProfessorAndRoleResponsable(any(), any())).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/activity/findAll/{userId}/{role}", deptHeadId, "jfedpto"))
            .andExpect(status().isNotFound()) // Debe esperar 404
            .andExpect(content().string("No actividades asignadas o creadas"));
    }
}