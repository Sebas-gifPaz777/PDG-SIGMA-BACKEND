package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program,Long> {
}
