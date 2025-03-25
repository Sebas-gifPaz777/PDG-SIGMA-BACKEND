package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    public Optional<Course> findByName(String name);

    public List<Course> findByProgram(Program program);


}
