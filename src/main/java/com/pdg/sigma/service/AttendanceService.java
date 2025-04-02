package com.pdg.sigma.service;

import java.util.List;
import java.util.Optional;

import com.pdg.sigma.domain.Attendance;

public interface AttendanceService extends GenericService<Attendance, Integer> {
    List<Attendance> findByActivity(Integer activityId);
    Optional<Attendance> findByActivityAndStudent(Integer activityId, String studentId);
}

