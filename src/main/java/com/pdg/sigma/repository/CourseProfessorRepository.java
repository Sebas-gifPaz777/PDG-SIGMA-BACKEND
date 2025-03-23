package com.pdg.sigma.repository;

import com.pdg.sigma.domain.CourseProfessor;
import com.pdg.sigma.domain.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseProfessorRepository extends JpaRepository<CourseProfessor, Integer> {


    List<CourseProfessor> findByProfessor(Professor professor);
}
