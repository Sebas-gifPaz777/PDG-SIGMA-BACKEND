package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.Monitoring;
import com.pdg.sigma.domain.Program;
import com.pdg.sigma.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonitoringRepository extends JpaRepository<Monitoring,Long> {
    public Optional<Monitoring> findByCourse(Course course);
    public List<Monitoring> findBySchool(School school);
    public List<Monitoring> findByProgram(Program program);

}
