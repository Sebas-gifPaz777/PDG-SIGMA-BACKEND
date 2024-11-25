package com.pdg.sigma.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "monitoring_student")
public class MonitoringStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "monitoring", nullable = false)
    private Monitoring monitoring;

    @ManyToOne
    @JoinColumn(name = "student", nullable = false)
    private Professor student;
}
