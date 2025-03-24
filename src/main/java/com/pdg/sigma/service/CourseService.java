package com.pdg.sigma.service;

import java.util.Optional;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.dto.CourseDTO;

public interface CourseService extends GenericService<CourseDTO, Long>{
    Optional<Course> findEntityById(Long id);
}
