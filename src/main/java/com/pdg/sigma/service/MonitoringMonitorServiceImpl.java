package com.pdg.sigma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pdg.sigma.repository.MonitoringMonitorRepository;
import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.domain.MonitoringMonitor;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonitoringMonitorServiceImpl {

    @Autowired
    private MonitoringMonitorRepository monitoringMonitorRepository;

    public List<Monitor> getMonitorsByMonitoringId(Long monitoringId) {
        List<MonitoringMonitor> monitoringMonitors = monitoringMonitorRepository.findByMonitoringId(monitoringId);
        return monitoringMonitors.stream()
                .map(MonitoringMonitor::getMonitor)
                .collect(Collectors.toList());
    }
}