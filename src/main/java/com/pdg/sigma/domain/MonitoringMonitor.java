package com.pdg.sigma.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "monitoring_monitor")
public class MonitoringMonitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "estado_seleccion", nullable = false)
    private String estadoSeleccion = "no seleccionado";

    @ManyToOne
    @JoinColumn(name = "monitoring_id", nullable = false)
    @JsonBackReference // Hijo
    private Monitoring monitoring;

    @ManyToOne
    @JoinColumn(name = "monitor_id", nullable = false)
    private Monitor monitor;

    public MonitoringMonitor(Monitoring monitoring,Monitor monitor){
        this.monitoring =  monitoring;
        this.monitor = monitor;
    }

    public MonitoringMonitor(Monitoring monitoring,Monitor monitor, String estadoSeleccion){
        this.monitoring =  monitoring;
        this.monitor = monitor;
        this.estadoSeleccion = estadoSeleccion;
    }
}
