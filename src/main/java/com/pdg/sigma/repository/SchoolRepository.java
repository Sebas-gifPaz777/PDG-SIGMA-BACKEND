package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Program;
import com.pdg.sigma.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School,Long> {
    public Optional<School> findByName(String name);
}
