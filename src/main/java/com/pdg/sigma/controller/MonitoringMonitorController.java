
package com.pdg.sigma.controller;

import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.dto.MonitorDTO;
import com.pdg.sigma.service.MonitoringMonitorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "https://pdg-sigma.vercel.app/")
@RequestMapping("/monitoring-monitor")
@RestController
public class MonitoringMonitorController {

    @Autowired
    private MonitoringMonitorServiceImpl monitoringMonitorService;

    @GetMapping("/{monitoringId}/monitors")
    public List<MonitorDTO> getMonitorsByMonitoring(@PathVariable Long monitoringId) {
        return monitoringMonitorService.getMonitorsByMonitoringId(monitoringId);
    }

    
}