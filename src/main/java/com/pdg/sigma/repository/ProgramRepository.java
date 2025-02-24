package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program,Long> {
    public Program findByName(String name);
}
