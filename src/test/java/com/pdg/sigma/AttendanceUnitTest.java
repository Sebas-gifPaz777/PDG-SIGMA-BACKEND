package com.pdg.sigma;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pdg.sigma.domain.Activity;
import com.pdg.sigma.domain.Attendance;
import com.pdg.sigma.domain.Student;
import com.pdg.sigma.repository.ActivityRepository;
import com.pdg.sigma.repository.AttendanceRepository;
import com.pdg.sigma.repository.StudentRepository;
import com.pdg.sigma.service.AttendanceServiceImpl;

@ExtendWith(MockitoExtension.class)
class AttendanceUnitTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    private Attendance attendance;
    private Activity activity;
    private Student student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        activity = new Activity();
        activity.setId(1);

        student = new Student();
        student.setCode("12345");

        attendance = new Attendance();
        attendance.setId(1);
        attendance.setActivity(activity);
        attendance.setStudent(student);
    }

    @Test
    void testFindByActivity() {
        when(attendanceRepository.findByActivityId(1)).thenReturn(List.of(attendance));

        List<Attendance> result = attendanceService.findByActivity(1);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(attendance, result.get(0));
    }

    @Test
    void testFindByActivityAndStudent() {
        when(attendanceRepository.findByActivityIdAndStudentCode(1, "12345")).thenReturn(Optional.of(attendance));

        Optional<Attendance> result = attendanceService.findByActivityAndStudent(1, "12345");

        assertTrue(result.isPresent());
        assertEquals(attendance, result.get());
    }

    @Test
    void testSaveAttendance() throws Exception {
        when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activity));
        when(studentRepository.findById(student.getCode())).thenReturn(Optional.of(student));

        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);

        Attendance savedAttendance = attendanceService.save(attendance);

        // Verificar resultados
        assertNotNull(savedAttendance);
        assertEquals(attendance.getId(), savedAttendance.getId());
    }

    @Test
    void testDeleteAttendance() throws Exception {
        doNothing().when(attendanceRepository).delete(any(Attendance.class));

        assertDoesNotThrow(() -> attendanceService.delete(attendance));

        verify(attendanceRepository, times(1)).delete(attendance);
    }
}
