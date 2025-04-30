package com.pdg.sigma.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Entity
@Getter
@NoArgsConstructor
@Table(name = "activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(100)")
    private String name;

    @Column(name = "creation_date", nullable = false)
    private Date creation;

    @Column(name = "finish_date", nullable = false)
    private Date finish;

    @Column(name = "role_creator", nullable = false, columnDefinition = "char(1)")
    private String roleCreator;

    @Column(name = "role_responsable", nullable = false, columnDefinition = "char(1)")
    private String roleResponsable;

    @Column(name = "category", nullable = true, columnDefinition = "varchar(30)")
    private String category;

    @Column(name = "description", nullable = false, columnDefinition = "varchar(255)")
    private String description;

    @ManyToOne
    @JoinColumn(name = "monitoring_id", nullable = false)
    private Monitoring monitoring;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "monitor_id")
    private Monitor monitor;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private StateActivity state;

    @Column(name = "semester", columnDefinition = "varchar(8)")
    private String semester;

    @Column(name = "delivey_date")
    private Date delivey;

    @Column(name = "edited_date")
    private Date edited;

    public Activity(String name, Date creation, Date finish, String roleCreator, String roleResponsable, String category, String description, Monitoring monitoring, Professor professor, Monitor monitor, StateActivity state, Date delivey, String semester,  Date edited) {
        
        this.name = name;
        this.creation = creation;
        this.finish = finish;
        this.roleCreator = roleCreator;
        this.roleResponsable = roleResponsable;
        this.category = category;
        this.description = description;
        this.monitoring = monitoring;
        this.professor = professor;
        this.monitor = monitor;
        this.state = state;
        this.delivey = delivey;
        this.semester = semester;
        this.edited = edited;
    }


}
