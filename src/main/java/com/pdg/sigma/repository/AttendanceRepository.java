package com.pdg.sigma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pdg.sigma.domain.Activity;
import com.pdg.sigma.domain.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByActivityId(Integer activityId);
    Optional<Attendance> findByActivityIdAndStudentCode(Integer activityId, String studentCode);
    List<Attendance> findByActivityIn(List<Activity> activities);
}
