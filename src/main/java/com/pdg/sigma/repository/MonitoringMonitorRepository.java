package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.domain.Monitoring;
import com.pdg.sigma.domain.MonitoringMonitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MonitoringMonitorRepository extends JpaRepository<MonitoringMonitor,Long> {

    public Optional<MonitoringMonitor> findByMonitoringAndMonitor(Monitoring monitoring, Monitor monitor);
    public List<MonitoringMonitor> findByMonitoring(Monitoring monitoring);

    public List<MonitoringMonitor> findByMonitor(Monitor monitor);
}
