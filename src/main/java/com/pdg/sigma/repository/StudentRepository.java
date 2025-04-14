package com.pdg.sigma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pdg.sigma.domain.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
}
