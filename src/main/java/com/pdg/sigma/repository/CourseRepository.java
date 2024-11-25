package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long> {
}
