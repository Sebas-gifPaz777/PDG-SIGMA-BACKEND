package com.pdg.sigma.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pdg.sigma.domain.StudentCourse;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
}
