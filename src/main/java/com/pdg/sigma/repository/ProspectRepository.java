package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Prospect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProspectRepository extends JpaRepository<Prospect,String> {
}
