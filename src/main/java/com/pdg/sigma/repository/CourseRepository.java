package com.pdg.sigma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.Program;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    public Optional<Course> findByName(String name);

    public List<Course> findByProgram(Program program);
    public List<Course> findByProgramId(Long programId);
    public List<Course> findByProgramIdIn(List<Long> programIds);
}
