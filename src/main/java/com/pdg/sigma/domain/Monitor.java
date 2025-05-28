package com.pdg.sigma.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "monitor")
public class Monitor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "code", nullable = false, columnDefinition = "varchar(20)")
    private String code;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(100)")
    private String name;

    @Column(name = "last_name", nullable = false, columnDefinition = "varchar(100)")
    private String lastName;

    @Column(name = "semester", nullable = false)
    private int semester;

    @Column(name = "grade_average", nullable = true)
    private double gradeAverage;

    @Column(name = "grade_course", nullable = true)
    private double gradeCourse;

    @Column(name = "email", nullable = false, columnDefinition = "varchar(100)")
    private String email;

    @Column(name = "id", nullable = false, columnDefinition = "varchar(100)")
    private String idMonitor;

    public Monitor(String code, String name, String lastName, int semester, double gradeAverage, double gradeCourse, String email, Monitoring monitoring, String idMonitor) {
        this.code = code;
        this.name = name;
        this.lastName = lastName;
        this.semester = semester;
        this.gradeAverage = gradeAverage;
        this.gradeCourse = gradeCourse;
        this.email = email;
        this.idMonitor = idMonitor;
    }
}


