package com.pdg.sigma.repository;

import com.pdg.sigma.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonitoringRepository extends JpaRepository<Monitoring,Long> {
    public Optional<Monitoring> findByCourse(Course course);
    public List<Monitoring> findBySchool(School school);
    public List<Monitoring> findByProgram(Program program);
    public List<Monitoring> findByProfessor(Professor professor);

    @Query("SELECT DISTINCT m FROM Monitoring m JOIN m.monitoringMonitors mm " +
            "WHERE m.professor.id = :professorId AND mm.estadoSeleccion = 'seleccionado'")
    List<Monitoring> findMonitoringsByProfessorAndHavingSelectedMonitors(@Param("professorId") String professorId);

    @Query("SELECT DISTINCT m FROM Monitoring m JOIN m.monitoringMonitors mm " +
           "WHERE mm.monitor.code = :monitorCode AND mm.estadoSeleccion = 'seleccionado'")
    List<Monitoring> findMonitoringsDirectlyAssignedToMonitorWithStatusSelected(@Param("monitorCode") String monitorCode);
}
