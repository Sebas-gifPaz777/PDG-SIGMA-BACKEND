package com.pdg.sigma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.CourseProfessor;
import com.pdg.sigma.domain.Professor;

@Repository
public interface CourseProfessorRepository extends JpaRepository<CourseProfessor, Integer> {
    List<CourseProfessor> findByProfessor(Professor professor);
    List<CourseProfessor> findByCourseId(Long courseId);

    @Query("SELECT cp.professor FROM CourseProfessor cp WHERE cp.course.id IN :courseIds")
    List<Professor> findProfessorsByCourseIds(@Param("courseIds") List<Long> courseIds);

    @Modifying
    @Transactional
    @Query("DELETE FROM CourseProfessor cp WHERE cp.course = :course AND cp.professor = :professor")
    void deleteByCourseAndProfessor(@Param("course") Course course, @Param("professor") Professor professor);
}
