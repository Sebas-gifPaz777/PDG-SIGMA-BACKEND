package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor,String> {
}
