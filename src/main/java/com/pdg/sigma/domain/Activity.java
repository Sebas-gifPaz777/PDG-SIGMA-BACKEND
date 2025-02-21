package com.pdg.sigma.domain;

import com.pdg.sigma.domain.Activity;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Entity
@Table(name = "activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(255)")
    private String name;

    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "finish_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishDate;

    @Column(name = "role_creator", nullable = false, columnDefinition = "CHAR(1)")
    private Character roleCreator;

    @Column(name = "role_responsable", nullable = false, columnDefinition = "CHAR(1)")
    private String roleResponsable;

    @Column(name = "category", columnDefinition = "varchar(30)")
    private String category;

    @Column(name = "description", columnDefinition = "varchar(255)")
    private String description;

    @Column(name = "monitoring_id", nullable = false)
    private Long monitoringId;

    @Column(name = "professor_id", nullable = false)
    private Long professorId;

    @Column(name = "monitor_id", nullable = false)
    private Long monitorId;

    @Column(name = "state", nullable = false, columnDefinition = "varchar(50)")
    private String state;

    // Constructor vacío (obligatorio para JPA)
    public MonitoringTask() {}

    // Constructor con parámetros
    public MonitoringTask(Long id, String name, Date creationDate, Date finishDate, String roleCreator, String roleResponsable, String category, String description, Long monitoringId, Long professorId, Long monitorId, String state) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.finishDate = finishDate;
        this.roleCreator = roleCreator;
        this.roleResponsable = roleResponsable;
        this.category = category;
        this.description = description;
        this.monitoringId = monitoringId;
        this.professorId = professorId;
        this.monitorId = monitorId;
        this.state = state;
    }
   
}
