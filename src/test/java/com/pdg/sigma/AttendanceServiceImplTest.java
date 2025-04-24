package com.pdg.sigma;

import com.pdg.sigma.domain.Activity;
import com.pdg.sigma.domain.Attendance;
import com.pdg.sigma.domain.Student;
import com.pdg.sigma.service.AttendanceServiceImpl;
import com.pdg.sigma.repository.ActivityRepository;
import com.pdg.sigma.repository.AttendanceRepository;
import com.pdg.sigma.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceImplTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    @Test
    void testSave_Success() {
        // Arrange
        Activity activity = new Activity();
        activity.setId(1);
        Student student = new Student();
        student.setCode("S123");
        Attendance attendanceToSave = new Attendance();
        attendanceToSave.setActivity(activity);
        attendanceToSave.setStudent(student);

        when(activityRepository.findById(1)).thenReturn(Optional.of(activity));
        when(studentRepository.findById("S123")).thenReturn(Optional.of(student));
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendanceToSave);

        // Act
        Attendance savedAttendance = attendanceService.save(attendanceToSave);

        // Assert
        assertNotNull(savedAttendance);
        assertEquals(activity, savedAttendance.getActivity());
        assertEquals(student, savedAttendance.getStudent());
        verify(attendanceRepository, times(1)).save(attendanceToSave);
    }

    @Test
    void testSave_ActivityNotFound() {
        // Arrange
        Activity activity = new Activity();
        activity.setId(1);
        Student student = new Student();
        student.setCode("S123");
        Attendance attendanceToSave = new Attendance();
        attendanceToSave.setActivity(activity);
        attendanceToSave.setStudent(student);

        when(activityRepository.findById(1)).thenReturn(Optional.empty());

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> attendanceService.save(attendanceToSave));
        assertEquals("Actividad no encontrada con ID: 1", exception.getMessage());
        verify(attendanceRepository, never()).save(any(Attendance.class));
    }

    @Test
    void testSave_StudentNotFound() {
        // Arrange
        Activity activity = new Activity();
        activity.setId(1);
        Student student = new Student();
        student.setCode("S123");
        Attendance attendanceToSave = new Attendance();
        attendanceToSave.setActivity(activity);
        attendanceToSave.setStudent(student);

        when(activityRepository.findById(1)).thenReturn(Optional.of(activity));
        when(studentRepository.findById("S123")).thenReturn(Optional.empty());

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> attendanceService.save(attendanceToSave));
        assertEquals("Estudiante no encontrado con ID: S123", exception.getMessage());
        verify(attendanceRepository, never()).save(any(Attendance.class));
    }

    @Test
    void testFindByActivity() {
        // Arrange
        Integer activityId = 1;
        Attendance attendance1 = new Attendance();
        Attendance attendance2 = new Attendance();
        List<Attendance> mockAttendances = Arrays.asList(attendance1, attendance2);

        when(attendanceRepository.findByActivityId(activityId)).thenReturn(mockAttendances);

        // Act
        List<Attendance> result = attendanceService.findByActivity(activityId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(mockAttendances, result);
        verify(attendanceRepository, times(1)).findByActivityId(activityId);
    }

    @Test
    void testFindByActivityAndStudent_Found() {
        // Arrange
        Integer activityId = 1;
        String studentId = "S123";
        Attendance mockAttendance = new Attendance();

        when(attendanceRepository.findByActivityIdAndStudentCode(activityId, studentId)).thenReturn(Optional.of(mockAttendance));

        // Act
        Optional<Attendance> result = attendanceService.findByActivityAndStudent(activityId, studentId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(mockAttendance, result.get());
        verify(attendanceRepository, times(1)).findByActivityIdAndStudentCode(activityId, studentId);
    }

    @Test
    void testFindByActivityAndStudent_NotFound() {
        // Arrange
        Integer activityId = 1;
        String studentId = "S123";

        when(attendanceRepository.findByActivityIdAndStudentCode(activityId, studentId)).thenReturn(Optional.empty());

        // Act
        Optional<Attendance> result = attendanceService.findByActivityAndStudent(activityId, studentId);

        // Assert
        assertFalse(result.isPresent());
        verify(attendanceRepository, times(1)).findByActivityIdAndStudentCode(activityId, studentId);
    }

    @Test
    void testFindAll() {
        // Arrange
        List<Attendance> mockAttendances = Arrays.asList(new Attendance(), new Attendance(), new Attendance());
        when(attendanceRepository.findAll()).thenReturn(mockAttendances);

        // Act
        List<Attendance> result = attendanceService.findAll();

        // Assert
        assertEquals(3, result.size());
        assertEquals(mockAttendances, result);
        verify(attendanceRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Found() {
        // Arrange
        Integer attendanceId = 1;
        Attendance mockAttendance = new Attendance();
        mockAttendance.setId(attendanceId);

        when(attendanceRepository.findById(attendanceId)).thenReturn(Optional.of(mockAttendance));

        // Act
        Optional<Attendance> result = attendanceService.findById(attendanceId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(mockAttendance, result.get());
        verify(attendanceRepository, times(1)).findById(attendanceId);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        Integer attendanceId = 1;
        when(attendanceRepository.findById(attendanceId)).thenReturn(Optional.empty());

        // Act
        Optional<Attendance> result = attendanceService.findById(attendanceId);

        // Assert
        assertFalse(result.isPresent());
        verify(attendanceRepository, times(1)).findById(attendanceId);
    }

    @Test
    void testUpdate() {
        // Arrange
        Attendance attendanceToUpdate = new Attendance();
        attendanceToUpdate.setId(1);
        when(attendanceRepository.save(attendanceToUpdate)).thenReturn(attendanceToUpdate);

        // Act
        Attendance updatedAttendance = attendanceService.update(attendanceToUpdate);

        // Assert
        assertNotNull(updatedAttendance);
        assertEquals(1, updatedAttendance.getId());
        verify(attendanceRepository, times(1)).save(attendanceToUpdate);
    }

    @Test
    void testDelete() {
        // Arrange
        Attendance attendanceToDelete = new Attendance();

        // Act
        attendanceService.delete(attendanceToDelete);

        // Assert
        verify(attendanceRepository, times(1)).delete(attendanceToDelete);
    }

    @Test
    void testDeleteById() {
        // Arrange
        Integer attendanceIdToDelete = 1;

        // Act
        attendanceService.deleteById(attendanceIdToDelete);

        // Assert
        verify(attendanceRepository, times(1)).deleteById(attendanceIdToDelete);
    }

    @Test
    void testCount() {
        // Arrange
        when(attendanceRepository.count()).thenReturn(5L);

        // Act
        Long count = attendanceService.count();

        // Assert
        assertEquals(5L, count);
        verify(attendanceRepository, times(1)).count();
    }
}