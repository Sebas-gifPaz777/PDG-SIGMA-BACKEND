package com.pdg.sigma.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdg.sigma.domain.Activity;
import com.pdg.sigma.domain.Attendance;
import com.pdg.sigma.domain.Student;
import com.pdg.sigma.repository.ActivityRepository;
import com.pdg.sigma.repository.AttendanceRepository;
import com.pdg.sigma.repository.StudentRepository;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ActivityRepository activityRepository; 

    @Autowired
    private StudentRepository studentRepository; 

    @Override
    public Attendance save(Attendance entity) {
        // Validar que la actividad y el estudiante existen
        Activity activity = activityRepository.findById(entity.getActivity().getId())
            .orElseThrow(() -> new RuntimeException("Actividad no encontrada con ID: " + entity.getActivity().getId()));
    
        Student student = studentRepository.findById(entity.getStudent().getCode())
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + entity.getStudent().getCode()));
    
        // Asignar las entidades recuperadas para evitar errores de persistencia
        entity.setActivity(activity);
        entity.setStudent(student);
    
        // Guardar la asistencia con relaciones correctamente establecidas
        return attendanceRepository.save(entity);
    }
    


    @Override
    public List<Attendance> findByActivity(Integer activityId) {
        return attendanceRepository.findByActivityId(activityId);
    }

    @Override
    public List<Attendance> findAll() {
        return attendanceRepository.findAll();
    }

    @Override
    public Optional<Attendance> findById(Integer id) {
        return attendanceRepository.findById(id);
    }


    @Override
    public Attendance update(Attendance entity) {
        return attendanceRepository.save(entity);
    }

    @Override
    public void delete(Attendance entity) {
        attendanceRepository.delete(entity);
    }

    @Override
    public void deleteById(Integer id) {
        attendanceRepository.deleteById(id);
    }

    @Override
    public void validate(Attendance entity) {
        
    }

    @Override
    public Long count() {
        return attendanceRepository.count();
    }
}
