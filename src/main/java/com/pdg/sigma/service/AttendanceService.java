package com.pdg.sigma.service;

import java.util.List;

import com.pdg.sigma.domain.Attendance;

public interface AttendanceService extends GenericService<Attendance, Integer> {
    List<Attendance> findByActivity(Integer activityId);
}
