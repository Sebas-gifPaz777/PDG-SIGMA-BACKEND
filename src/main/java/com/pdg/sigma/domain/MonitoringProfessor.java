package com.pdg.sigma.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "monitoring_professor")
public class MonitoringProfessor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "monitoring", nullable = false)
    private Monitoring monitoring;

    @ManyToOne
    @JoinColumn(name = "professor", nullable = false)
    private Professor professor;
}
