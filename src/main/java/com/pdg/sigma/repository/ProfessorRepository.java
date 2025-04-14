package com.pdg.sigma.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pdg.sigma.domain.Professor;

public interface ProfessorRepository extends JpaRepository<Professor,String> {
    
}
