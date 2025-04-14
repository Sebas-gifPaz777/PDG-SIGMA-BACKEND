package com.pdg.sigma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pdg.sigma.domain.DepartmentHead;

@Repository
public interface DepartmentHeadRepository extends JpaRepository<DepartmentHead, String> {
    
}
