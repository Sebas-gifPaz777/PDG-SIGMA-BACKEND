package com.pdg.sigma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pdg.sigma.domain.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByActivityId(Integer activityId);
}
