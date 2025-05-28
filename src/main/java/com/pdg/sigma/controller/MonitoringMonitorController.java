
package com.pdg.sigma.controller;

import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.dto.MonitorDTO;
import com.pdg.sigma.service.MonitoringMonitorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
@RequestMapping("/monitoring-monitor")
@RestController
public class MonitoringMonitorController {

    @Autowired
    private MonitoringMonitorServiceImpl monitoringMonitorService;

    @GetMapping("/{monitoringId}/monitors")
    public List<MonitorDTO> getMonitorsByMonitoring(@PathVariable Long monitoringId) {
        return monitoringMonitorService.getMonitorsByMonitoringId(monitoringId);
    }

    @DeleteMapping(value= "/{idMonitoring}/{idMonitor}")
    public ResponseEntity<?> deleteMonitorRelation(@PathVariable Long idMonitoring, @PathVariable String idMonitor){
        try {
            monitoringMonitorService.deleteRelation(idMonitoring, idMonitor);
            return ResponseEntity.status(200).body("Relación eliminada exitosamente");
        } catch (Exception e) {
             e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    
}