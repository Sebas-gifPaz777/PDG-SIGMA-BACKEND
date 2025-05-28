package com.pdg.sigma.repository;

import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.domain.Monitoring;
import com.pdg.sigma.domain.MonitoringMonitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MonitoringMonitorRepository extends JpaRepository<MonitoringMonitor,Long> {

    public Optional<MonitoringMonitor> findByMonitoringAndMonitor(Monitoring monitoring, Monitor monitor);
    public Optional<MonitoringMonitor> findByMonitoringIdAndMonitorCode(Long monitoringId, String monitorCode);
    public List<MonitoringMonitor> findByMonitoring(Monitoring monitoring);
    public List<MonitoringMonitor> findByMonitoringId(Long monitoringId);
    
    public List<MonitoringMonitor> findByMonitor(Monitor monitor);
    void deleteByMonitoring(Monitoring monitoring);

    @Transactional
    void deleteByMonitoringIdAndMonitor_Code(Long monitoringId, String monitorCode);

}
