package com.pdg.sigma;

import com.pdg.sigma.domain.Activity;
import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.dto.ActivityRequestDTO;
import com.pdg.sigma.dto.NewActivityRequestDTO;
import com.pdg.sigma.repository.ActivityRepository;
import com.pdg.sigma.repository.MonitorRepository;
import com.pdg.sigma.repository.MonitoringRepository;
import com.pdg.sigma.repository.ProfessorRepository;
import com.pdg.sigma.service.ActivityServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = "com.pdg.sigma.util")
public class ActivityTest {

    @InjectMocks
    private ActivityServiceImpl activityService;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private MonitorRepository monitorRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private MonitoringRepository monitoringRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAssignRoles_MonitorCreator_ProfessorResponsible() throws Exception {
        Activity activity = new Activity();
        Monitor creatorMonitor = new Monitor();
        creatorMonitor.setIdMonitor("MON001");
        Professor responsibleProfessor = new Professor();
        responsibleProfessor.setId("PROF001");

        activity.setMonitor(creatorMonitor);
        activity.setProfessor(responsibleProfessor);

        assertEquals(creatorMonitor, activity.getMonitor());
        assertEquals(responsibleProfessor, activity.getProfessor());
    }

    @Test
    void testAssignRoles_ProfessorCreator_MonitorResponsible() throws Exception {
        Activity activity = new Activity();
        Professor creatorProfessor = new Professor();
        creatorProfessor.setId("PROF002");
        Monitor responsibleMonitor = new Monitor();
        responsibleMonitor.setIdMonitor("MON002");

        activity.setMonitor(responsibleMonitor);
        activity.setProfessor(creatorProfessor);

        assertEquals(responsibleMonitor, activity.getMonitor());
        assertEquals(creatorProfessor, activity.getProfessor());
    }

    @Test
    void testAssignRoles_MonitorCreator_MonitorResponsible() throws Exception {
        Activity activity = new Activity();
        Monitor creatorMonitor = new Monitor();
        creatorMonitor.setIdMonitor("MON003");

        activity.setMonitor(creatorMonitor);
        activity.setProfessor(null);

        assertEquals(creatorMonitor, activity.getMonitor());
        assertNull(activity.getProfessor());
    }

    @Test
    void testAssignRoles_ProfessorCreator_ProfessorResponsible() throws Exception {
        Activity activity = new Activity();
        Professor creatorProfessor = new Professor();
        creatorProfessor.setId("PROF003");
        
        activity.setMonitor(null);
        activity.setProfessor(creatorProfessor);

        assertNull(activity.getMonitor());
        assertEquals(creatorProfessor, activity.getProfessor());
    }

    // @Test
    // void testSave_NewActivity() throws Exception {
    //     NewActivityRequestDTO dto = new NewActivityRequestDTO();
    //     dto.setName("Tarea 1");
    //     dto.setFinish(new Date(System.currentTimeMillis() + 86400000)); // Tomorrow
    //     dto.setRoleCreator("M");
    //     dto.setRoleResponsable("P");
    //     dto.setCategory("Entrega");
    //     dto.setDescription("Primera tarea");
    //     dto.setMonitoringId(1);
    //     dto.setProfessorId("PROF004");
    //     dto.setMonitorId("MON004");
    //     dto.setDelivey(new Date(System.currentTimeMillis() + 172800000)); // Day after tomorrow
    //     dto.setSemester("2025-I");

    //     Monitoring monitoring = new Monitoring();
    //     monitoring.setId(1L);
    //     Monitor creatorMonitor = new Monitor();
    //     creatorMonitor.setIdMonitor("MON004");
    //     Professor responsibleProfessor = new Professor();
    //     responsibleProfessor.setId("PROF004");
    //     Activity savedActivity = new Activity();
    //     savedActivity.setId(1);
    //     savedActivity.setName(dto.getName());
    //     savedActivity.setMonitoring(monitoring);
    //     savedActivity.setMonitor(creatorMonitor);
    //     savedActivity.setProfessor(responsibleProfessor);
    //     savedActivity.setRoleCreator(dto.getRoleCreator());
    //     savedActivity.setRoleResponsable(dto.getRoleResponsable());
    //     savedActivity.setState(StateActivity.PENDIENTE);
    //     savedActivity.setDelivey(dto.getDelivey());
    //     savedActivity.setSemester(dto.getSemester());

    //     when(monitoringRepository.findById(1L)).thenReturn(Optional.of(monitoring));
    //     when(monitorRepository.findByIdMonitor("MON004")).thenReturn(Optional.of(creatorMonitor));
    //     when(professorRepository.findById("PROF004")).thenReturn(Optional.of(responsibleProfessor));
    //     when(activityRepository.save(any(Activity.class))).thenReturn(savedActivity);

    //     ActivityDTO result = activityService.save(dto);

    //     assertNotNull(result);
    //     assertEquals(savedActivity.getName(), result.getName());
    // }

    // @Test
    // void testUpdate_ExistingActivity() throws Exception {
    //     ActivityRequestDTO dto = new ActivityRequestDTO();
    //     dto.setId(1);
    //     dto.setName("Tarea 1 Actualizada");
    //     dto.setState("COMPLETADO");
    //     dto.setMonitoringId(2);
    //     dto.setMonitorId("MON005");
    //     dto.setRoleCreator("P");
    //     dto.setRoleResponsable("M");

    //     Activity existingActivity = new Activity();
    //     existingActivity.setId(1);
    //     existingActivity.setName("Tarea 1");
    //     existingActivity.setState(StateActivity.PENDIENTE);
    //     Monitoring existingMonitoring = new Monitoring();
    //     existingMonitoring.setId(1L);
    //     existingActivity.setMonitoring(existingMonitoring);

    //     Monitoring updatedMonitoring = new Monitoring();
    //     updatedMonitoring.setId(2L);
    //     Monitor responsibleMonitor = new Monitor();
    //     responsibleMonitor.setIdMonitor("MON005");
    //     Professor creatorProfessor = new Professor();
    //     creatorProfessor.setId("PROF005");
    //     Activity updatedActivity = new Activity();
    //     updatedActivity.setId(1);
    //     updatedActivity.setName(dto.getName());
    //     updatedActivity.setState(StateActivity.COMPLETADO);
    //     updatedActivity.setMonitoring(updatedMonitoring);
    //     updatedActivity.setMonitor(responsibleMonitor);
    //     updatedActivity.setProfessor(creatorProfessor);
    //     updatedActivity.setRoleCreator(dto.getRoleCreator());
    //     updatedActivity.setRoleResponsable(dto.getRoleResponsable());

    //     when(activityRepository.findById(1)).thenReturn(Optional.of(existingActivity));
    //     when(monitoringRepository.findById(2L)).thenReturn(Optional.of(updatedMonitoring));
    //     when(monitorRepository.findByIdMonitor("MON005")).thenReturn(Optional.of(responsibleMonitor));
    //     when(professorRepository.findById("PROF005")).thenReturn(Optional.of(creatorProfessor));
    //     when(activityRepository.save(any(Activity.class))).thenReturn(updatedActivity);

    //     ActivityDTO result = activityService.update(dto);

    //     assertNotNull(result);
    //     assertEquals(updatedActivity.getName(), result.getName());
    //     assertEquals(updatedActivity.getState().toString(), result.getState());
    //     assertEquals(updatedActivity.getMonitoring().getId(), result.getMonitoring().getId());
    // }

    @Test
    void testUpdate_ActivityNotFound() {
        ActivityRequestDTO dto = new ActivityRequestDTO();
        dto.setId(99);

        when(activityRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> activityService.update(dto));
        //assertEquals("Activity not found with id: 99", exception.getMessage());
    }

}