package com.pdg.sigma;

import com.pdg.sigma.domain.*;
import com.pdg.sigma.dto.ReportDTO;
import com.pdg.sigma.repository.ActivityRepository;
import com.pdg.sigma.repository.MonitoringRepository;
import com.pdg.sigma.repository.ProfessorRepository;
import com.pdg.sigma.service.MonitoringServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfessorReportTest {

    @Mock
    private ProfessorRepository professorRepository;
    @Mock
    private MonitoringRepository monitoringRepository;
    @Mock
    private ActivityRepository activityRepository;
    @InjectMocks
    private MonitoringServiceImpl reportService;

    private Professor professor;
    private Monitoring monitoring;
    private Course course;
    private Program program;

    @BeforeEach
    void setup() {
        professor = new Professor();
        professor.setId("prof123");
        professor.setName("Dr. John Doe");

        program = new Program();
        program.setName("Ingeniería de Sistemas");

        course = new Course();
        course.setName("Estructuras de Datos");
        course.setProgram(program);

        monitoring = new Monitoring();
        monitoring.setCourse(course);
    }

    @Test
    void testGetProfessorReport_Success() throws Exception {
        Activity act1 = new Activity();
        act1.setMonitoring(monitoring);
        act1.setState(StateActivity.PENDIENTE);

        Activity act2 = new Activity();
        act2.setMonitoring(monitoring);
        act2.setState(StateActivity.COMPLETADO);

        Activity act3 = new Activity();
        act3.setMonitoring(monitoring);
        act3.setState(StateActivity.COMPLETADOT);

        List<Activity> activities = List.of(act1, act2, act3);

        when(professorRepository.findById("prof123")).thenReturn(Optional.of(professor));
        when(monitoringRepository.findByProfessor(professor)).thenReturn(List.of(monitoring));
        when(activityRepository.findByProfessorAndRoleResponsable(professor, "P")).thenReturn(activities);
        when(activityRepository.findByProfessorAndRoleCreator(professor, "P")).thenReturn(Collections.emptyList());

        // Opcional: si `filterAssigned` está definido aparte
        // when(reportService.filterAssigned(...)).thenCallRealMethod();

        List<ReportDTO> result = reportService.getProfessorReport("prof123");

        assertEquals(1, result.size());
        ReportDTO report = result.get(0);
        assertEquals(1, report.getPending());
        assertEquals(1, report.getCompleted());
        assertEquals(1, report.getLate());
        assertEquals("Dr. John Doe", report.getName());
        assertEquals("prof123", report.getIdProfessor());
        assertEquals("Estructuras de Datos", report.getCourse());
        assertEquals("Ingeniería de Sistemas", report.getProgram());
    }

    @Test
    void testGetProfessorReport_NoProfessorFound() {
        when(professorRepository.findById("unknown")).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> reportService.getProfessorReport("unknown"));
        assertEquals("No existe professor con este id", exception.getMessage());
    }

    @Test
    void testGetProfessorReport_NoMonitorings() {
        when(professorRepository.findById("prof123")).thenReturn(Optional.of(professor));
        when(monitoringRepository.findByProfessor(professor)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(Exception.class, () -> reportService.getProfessorReport("prof123"));
        assertEquals("No hay monitorías creadas", exception.getMessage());
    }
}
