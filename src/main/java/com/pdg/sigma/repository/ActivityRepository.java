package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Activity;
import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.domain.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    public List<Activity> findByMonitorAndRoleCreator(Monitor monitor, String role);
    public List<Activity> findByMonitorAndRoleResponsable(Monitor monitor, String role);

    public List<Activity> findByProfessorAndRoleCreator(Professor monitor, String role);
    public List<Activity> findByProfessorAndRoleResponsable(Professor monitor, String role);


}
