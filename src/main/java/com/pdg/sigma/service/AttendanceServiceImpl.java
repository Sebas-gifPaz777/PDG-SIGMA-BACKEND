package com.pdg.sigma.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdg.sigma.domain.Attendance;
import com.pdg.sigma.repository.AttendanceRepository;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

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
    public Attendance save(Attendance entity) {
        return attendanceRepository.save(entity);
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
