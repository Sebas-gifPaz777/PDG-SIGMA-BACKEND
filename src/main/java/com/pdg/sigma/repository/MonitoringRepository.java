package com.pdg.sigma.repository;

import com.pdg.sigma.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonitoringRepository extends JpaRepository<Monitoring,Long> {
    public Optional<Monitoring> findByCourse(Course course);
    public List<Monitoring> findBySchool(School school);
    public List<Monitoring> findByProgram(Program program);
    public List<Monitoring> findByProfessor(Professor professor);

}
