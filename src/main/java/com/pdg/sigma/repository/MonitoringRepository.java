package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.Monitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonitoringRepository extends JpaRepository<Monitoring,Long> {
    public Optional<Monitoring> findByCourse(Course course);

}
