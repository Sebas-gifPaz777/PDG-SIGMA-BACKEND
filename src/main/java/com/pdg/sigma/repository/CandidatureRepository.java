package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatureRepository extends JpaRepository<Candidature,String> {
}
