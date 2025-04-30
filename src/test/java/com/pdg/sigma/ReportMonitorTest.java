package com.pdg.sigma;

import com.pdg.sigma.domain.*;
import com.pdg.sigma.dto.ReportDTO;
import com.pdg.sigma.repository.*;
import com.pdg.sigma.service.MonitoringServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static reactor.core.publisher.Mono.when;

@SpringBootTest(classes = SigmaApplication.class)
@AutoConfigureMockMvc
public class ReportMonitorTest {

    @Mock
    private MonitoringRepository monitoringRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private MonitoringMonitorRepository monitoringMonitorRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private DepartmentHeadRepository departmentHeadRepository;

    @Mock
    private HeadProgramRepository headProgramRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseProfessorRepository courseProfessorRepository;

    @InjectMocks
    private MonitoringServiceImpl monitoringService;

    @Test
    void testGetReportMonitorsWithProfessor() throws Exception {
        String professorId = "prof123";
        String role = "professor";

        // Simular profesor
        Professor professor = new Professor();
        professor.setId(professorId);
        professor.setName("John Professor");

        // Simular monitoreo
        Monitoring monitoring = new Monitoring();
        monitoring.setId(1L);
        Course course = new Course();
        course.setName("Algoritmos");
        Program program = new Program();
        program.setName("Ingeniería de Sistemas");
        course.setProgram(program);
        monitoring.setCourse(course);
        monitoring.setSemester("2025-1");
        monitoring.setProfessor(professor);

        // Simular monitor de la monitoría
        Monitor monitorEntity = new Monitor();
        monitorEntity.setName("Carlos Monitor");

        MonitoringMonitor monitoringMonitor = new MonitoringMonitor();
        monitoringMonitor.setMonitor(monitorEntity);
        monitoringMonitor.setMonitoring(monitoring);

        // Simular actividades
        Activity activity1 = new Activity();
        activity1.setState(StateActivity.PENDIENTE);
        activity1.setMonitoring(monitoring);

        Activity activity2 = new Activity();
        activity2.setState(StateActivity.COMPLETADO);
        activity2.setMonitoring(monitoring);

        Activity activity3 = new Activity();
        activity3.setState(StateActivity.COMPLETADOT);
        activity3.setMonitoring(monitoring);

        // Mockeos
        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        when(monitoringRepository.findByProfessor(professor)).thenReturn(List.of(monitoring));
        when(monitoringMonitorRepository.findByMonitoring(monitoring)).thenReturn(List.of(monitoringMonitor));
        when(activityRepository.findByMonitorAndRoleResponsable(monitorEntity, "M")).thenReturn(List.of(activity1,activity2, activity3));
        when(activityRepository.findByMonitorAndRoleCreator(monitorEntity, "M")).thenReturn(List.of());

        // Ejecutar método
        List<ReportDTO> result = monitoringService.getReportMonitors(professorId, role);

        // Verificar resultado
        assertEquals(1, result.size());
        ReportDTO report = result.get(0);
        assertEquals("Carlos Monitor", report.getName());
        assertEquals("Algoritmos", report.getCourse());
        assertEquals("Ingeniería de Sistemas", report.getProgram());
        assertEquals("John Professor", report.getProfessor());
        assertEquals("2025-1", report.getSemester());
        assertEquals(1, report.getPending());
        assertEquals(1, report.getCompleted());
        assertEquals(1, report.getLate());
        assertEquals("Carlos Monitor - Algoritmos", report.getNameAndCourse());
    }

    @Test
    void testGetReportMonitorsWithDepartmentHeadNotFound() {
        String headId = "999";
        String role = "head";

        when(departmentHeadRepository.findById(headId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            monitoringService.getReportMonitors(headId, role);
        });

        assertEquals("No existe un jefe con este Id", exception.getMessage());
    }
}
