
package com.pdg.sigma.controller;

import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.service.MonitoringMonitorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/monitoring-monitor")
@RestController
public class MonitoringMonitorController {

    @Autowired
    private MonitoringMonitorServiceImpl monitoringMonitorService;

    @GetMapping("/{monitoringId}/monitors")
    public List<Monitor> getMonitorsByMonitoring(@PathVariable Long monitoringId) {
        return monitoringMonitorService.getMonitorsByMonitoringId(monitoringId);
    }

    
}