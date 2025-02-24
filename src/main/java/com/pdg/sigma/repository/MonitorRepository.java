package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor,String> {

    public Optional<Monitor> findByCode(String code);
}
