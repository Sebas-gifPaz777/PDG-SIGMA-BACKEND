package com.pdg.sigma.repository;

import java.util.List;

import com.pdg.sigma.domain.HeadProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeadProgramRepository extends JpaRepository<HeadProgram, Integer> {
    List<HeadProgram> findByDepartmentHeadId(String departmentHeadId);
}
