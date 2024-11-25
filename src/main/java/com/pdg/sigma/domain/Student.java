package com.pdg.sigma.domain;

import com.pdg.sigma.domain.Monitoring;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "varchar(30)")
    private String id;

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

    @Column(name = "password", nullable = false)
    private String password;
}