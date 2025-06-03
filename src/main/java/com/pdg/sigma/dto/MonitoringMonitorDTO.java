package com.pdg.sigma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonitoringMonitorDTO {
    private Long idMonitoring;
    private String code; 
    private String estadoSeleccion;
}
