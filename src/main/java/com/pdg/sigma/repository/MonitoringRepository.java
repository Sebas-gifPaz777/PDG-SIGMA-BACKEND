package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Candidature;
import com.pdg.sigma.domain.Monitoring;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoringRepository extends JpaRepository<Monitoring,Long> {
}
