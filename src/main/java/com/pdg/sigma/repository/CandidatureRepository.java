package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature,String> {

    public Optional<Candidature> findByCode(String code);
}
