package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.MonitoringProfessor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoringProfessorRepository extends JpaRepository<MonitoringProfessor,Long> {
}
