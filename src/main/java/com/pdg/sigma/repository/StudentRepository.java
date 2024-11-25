package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,String> {
}
