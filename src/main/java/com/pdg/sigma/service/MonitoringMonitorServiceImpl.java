package com.pdg.sigma.service;

import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.dto.MonitorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pdg.sigma.repository.MonitoringMonitorRepository;

import jakarta.persistence.EntityNotFoundException;

import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.domain.MonitoringMonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonitoringMonitorServiceImpl {

    @Autowired
    private MonitoringMonitorRepository monitoringMonitorRepository;

    // public List<MonitorDTO> getMonitorsByMonitoringId(Long monitoringId) {
    //     List<MonitoringMonitor> monitoringMonitors = monitoringMonitorRepository.findByMonitoringId(monitoringId);
    //     List<MonitorDTO> monitors = new ArrayList<>();
    //     Professor professor = monitoringMonitors.get(0).getMonitoring().getProfessor();
    //     monitors.add(new MonitorDTO(professor.getName()+" "+professor.getId(), professor.getId(), "P"));

    //     for(MonitoringMonitor monitoringMonitor: monitoringMonitors){
    //         String name = monitoringMonitor.getMonitor().getName()+" "+monitoringMonitor.getMonitor().getLastName()+" "+monitoringMonitor.getMonitor().getCode();
    //         String id = monitoringMonitor.getMonitor().getIdMonitor();
    //         monitors.add(new MonitorDTO(name,id,"M"));
    //     }
    //     return monitors;
    // }

    public List<MonitorDTO> getMonitorsByMonitoringId(Long monitoringId) {
        List<MonitoringMonitor> monitoringMonitors = monitoringMonitorRepository.findByMonitoringId(monitoringId);
        List<MonitorDTO> monitors = new ArrayList<>();

        Professor professor = monitoringMonitors.get(0).getMonitoring().getProfessor();
        monitors.add(new MonitorDTO(professor.getName() + " " + professor.getId(), professor.getId(), "P")); 

        for (MonitoringMonitor monitoringMonitor : monitoringMonitors) {
            String name = monitoringMonitor.getMonitor().getName() + " " +
                        monitoringMonitor.getMonitor().getLastName() + " " +
                        monitoringMonitor.getMonitor().getCode();
            String id = monitoringMonitor.getMonitor().getIdMonitor();
            String selectionStatus = monitoringMonitor.getEstadoSeleccion(); 

            monitors.add(new MonitorDTO(name, id, "M"));
        }

        return monitors;
    }

    public void deleteRelation(Long idMonitoring, String monitorCode) throws Exception {
        monitoringMonitorRepository.deleteByMonitoringIdAndMonitor_Code(idMonitoring, monitorCode.trim());
    }

    public void updateApplicantSelectionStatus(Long monitoringId, String monitorCode, String newStatus) {
    MonitoringMonitor mm = monitoringMonitorRepository.findByMonitoringIdAndMonitorCode(monitoringId, monitorCode)
        .orElseThrow(() -> new EntityNotFoundException("MonitoringMonitor relation not found for monitoringId " + monitoringId + " and monitor code " + monitorCode));

    mm.setEstadoSeleccion(newStatus);
    monitoringMonitorRepository.save(mm);
}


}