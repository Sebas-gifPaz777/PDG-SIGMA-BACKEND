package com.pdg.sigma;

import com.pdg.sigma.domain.*;
import com.pdg.sigma.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MonitoringControllerReportTest {

    @Autowired
    private MockMvc mockMvc;

    // --- Mockeamos la capa de persistencia completa ---
    @MockBean private MonitoringRepository monitoringRepository;
    @MockBean private ProfessorRepository professorRepository;
    @MockBean private DepartmentHeadRepository departmentHeadRepository;
    @MockBean private HeadProgramRepository headProgramRepository;
    @MockBean private CourseRepository courseRepository;
    @MockBean private CourseProfessorRepository courseProfessorRepository;
    @MockBean private ActivityRepository activityRepository;
    @MockBean private AttendanceRepository attendanceRepository;
    @MockBean private SchoolRepository schoolRepository;
    @MockBean private ProgramRepository programRepository;
    @MockBean private MonitoringMonitorRepository monitoringMonitorRepository;
    @MockBean private MonitorRepository monitorRepository;

    private String professorId;
    private String departmentHeadId;
    private Long monitoringId;

    // --- Objetos de prueba reutilizables ---
    private Professor mockProfessor;
    private DepartmentHead mockDepartmentHead;
    private Monitoring mockMonitoring;
    private Course mockCourse;

    @BeforeEach
    void setUp() {
        professorId = "prof123";
        departmentHeadId = "jefeDpto789";
        monitoringId = 1L;

        mockProfessor = new Professor();
        mockProfessor.setId(professorId);
        mockProfessor.setName("Profesor Mock");

        mockDepartmentHead = new DepartmentHead();
        mockDepartmentHead.setId(departmentHeadId);

        mockCourse = new Course();
        mockCourse.setId(1L);
        mockCourse.setName("Cálculo I");

        mockMonitoring = new Monitoring();
        mockMonitoring.setId(monitoringId);
        mockMonitoring.setProfessor(mockProfessor);
        mockMonitoring.setCourse(mockCourse);
    }

    @Nested
    @DisplayName("Tests for Category Reports")
    class CategoryReportTests {

        @Test
        @WithMockUser
        @DisplayName("GET /getCategoriesReport/professor/{id} - Success")
        void getProfessorCategoriesReport_Success() throws Exception {
            // Arrange
            Activity mockActivity = new Activity();
            mockActivity.setCategory("Test Categoria");
            mockActivity.setMonitoring(mockMonitoring);

            when(professorRepository.findById(professorId)).thenReturn(Optional.of(mockProfessor));
            when(monitoringRepository.findByProfessor(mockProfessor)).thenReturn(List.of(mockMonitoring));
            when(activityRepository.findByMonitoring(mockMonitoring)).thenReturn(List.of(mockActivity));

            // Act & Assert
            mockMvc.perform(get("/monitoring/getCategoriesReport/professor/{professorId}", professorId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totales_por_categoria[0].categoria").value("Test Categoria"))
                    .andExpect(jsonPath("$.totales_por_categoria[0].cantidad_total").value(1));
        }

        @Test
        @WithMockUser
        @DisplayName("GET /getCategoriesReport/jfedpto/{id} - Success")
        void getDepartmentHeadCategoriesReport_Success() throws Exception {
            // Arrange
            Program program = new Program();
            HeadProgram headProgram = new HeadProgram();
            headProgram.setProgram(program);
            // CourseProfessor courseProfessor = new CourseProfessor(mockCourse, mockProfessor);
            CourseProfessor courseProfessor = new CourseProfessor(0, mockCourse, mockProfessor);
            Activity activity = new Activity();
            activity.setCategory("Jefe Dpto Categoria");
            activity.setMonitoring(mockMonitoring);

            when(departmentHeadRepository.findById(departmentHeadId)).thenReturn(Optional.of(mockDepartmentHead));
            when(headProgramRepository.findByDepartmentHeadId(departmentHeadId)).thenReturn(List.of(headProgram));
            when(courseRepository.findByProgram(program)).thenReturn(List.of(mockCourse));
            when(courseProfessorRepository.findByCourseId(mockCourse.getId())).thenReturn(List.of(courseProfessor));
            when(monitoringRepository.findByProfessor(mockProfessor)).thenReturn(List.of(mockMonitoring));
            when(activityRepository.findByMonitoring(mockMonitoring)).thenReturn(List.of(activity));

            // Act & Assert
            mockMvc.perform(get("/monitoring/getCategoriesReport/jfedpto/{departmentHeadId}", departmentHeadId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totales_por_categoria[0].categoria").value("Jefe Dpto Categoria"));
        }

        @Test
        @WithMockUser
        @DisplayName("GET /getCategoriesReport/professor/{id} - Professor Not Found")
        void getProfessorCategoriesReport_NotFound() throws Exception {
            // Arrange
            when(professorRepository.findById(professorId)).thenReturn(Optional.empty());

            // Act & Assert
            mockMvc.perform(get("/monitoring/getCategoriesReport/professor/{professorId}", professorId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("Profesor con ID " + professorId + " no encontrado."));
        }
    }

    @Nested
    @DisplayName("Tests for Attendance Reports")
    class AttendanceReportTests {

        @Test
        @WithMockUser
        @DisplayName("GET /getAttendanceReport/professor/{id} - Success")
        void getAttendanceReport_ForProfessor_Success() throws Exception {
            // Arrange
            Activity activity = new Activity();
            activity.setMonitoring(mockMonitoring);
            activity.setDelivey(Date.from(Instant.parse("2024-06-15T12:00:00Z"))); // Junio 2024
            
            Attendance attendance = new Attendance();
            attendance.setActivity(activity);
            Student student = new Student();
            student.setName("Estudiante Uno");
            attendance.setStudent(student);

            when(professorRepository.findById(professorId)).thenReturn(Optional.of(mockProfessor));
            when(monitoringRepository.findByProfessor(mockProfessor)).thenReturn(List.of(mockMonitoring));
            when(activityRepository.findByMonitoring(mockMonitoring)).thenReturn(List.of(activity));
            when(attendanceRepository.findByActivityIn(List.of(activity))).thenReturn(List.of(attendance));

            // Act & Assert
            mockMvc.perform(get("/monitoring/getAttendanceReport/{role}/{userId}", "professor", professorId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].mes").value("Junio"))
                    .andExpect(jsonPath("$[0].total_mes").value(1));
        }

        @Test
        @WithMockUser
        @DisplayName("GET /getAttendanceReport/jfedpto/{id} - Success")
        void getAttendanceReport_ForDeptHead_Success() throws Exception {
            // Arrange
            Program program = new Program();
            HeadProgram headProgram = new HeadProgram();
            headProgram.setProgram(program);
            // CourseProfessor courseProfessor = new CourseProfessor(mockCourse, mockProfessor);
            CourseProfessor courseProfessor = new CourseProfessor(0, mockCourse, mockProfessor);
            Activity activity = new Activity();
            activity.setMonitoring(mockMonitoring);
            activity.setDelivey(Date.from(Instant.parse("2024-07-15T12:00:00Z"))); // Julio 2024
            Attendance attendance = new Attendance();
            attendance.setActivity(activity);
            Student student = new Student();
            student.setName("Estudiante Dos");
            attendance.setStudent(student);

            when(departmentHeadRepository.findById(departmentHeadId)).thenReturn(Optional.of(mockDepartmentHead));
            when(headProgramRepository.findByDepartmentHeadId(departmentHeadId)).thenReturn(List.of(headProgram));
            when(courseRepository.findByProgram(program)).thenReturn(List.of(mockCourse));
            when(courseProfessorRepository.findByCourseId(mockCourse.getId())).thenReturn(List.of(courseProfessor));
            when(monitoringRepository.findByProfessor(mockProfessor)).thenReturn(List.of(mockMonitoring));
            when(activityRepository.findByMonitoring(mockMonitoring)).thenReturn(List.of(activity));
            when(attendanceRepository.findByActivityIn(List.of(activity))).thenReturn(List.of(attendance));
            
            // Act & Assert
            mockMvc.perform(get("/monitoring/getAttendanceReport/{role}/{userId}", "jfedpto", departmentHeadId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].mes").value("Julio"))
                    .andExpect(jsonPath("$[0].asistencia_por_curso[0].curso").value("Cálculo I"))
                    .andExpect(jsonPath("$[0].asistencia_por_curso[0].cantidad").value(1));
        }

        @Test
        @WithMockUser
        @DisplayName("GET /getAttendanceReport/{role}/{id} - Empty Report Returns OK with empty list")
        void getAttendanceReport_EmptyReport() throws Exception {
            // Arrange
            when(professorRepository.findById(professorId)).thenReturn(Optional.of(mockProfessor));
            when(monitoringRepository.findByProfessor(mockProfessor)).thenReturn(Collections.emptyList()); // No monitorings

            // Act & Assert
            mockMvc.perform(get("/monitoring/getAttendanceReport/{role}/{userId}", "professor", professorId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());
        }
        
        @Test
        @WithMockUser
        @DisplayName("GET /getAttendanceReport/{role}/{id} - Invalid Role")
        void getAttendanceReport_InvalidRole() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/monitoring/getAttendanceReport/{role}/{userId}", "monitor", "someId"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Rol no válido proporcionado: monitor"));
        }
    }
}