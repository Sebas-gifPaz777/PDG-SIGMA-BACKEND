package com.pdg.sigma.domain;

import com.pdg.sigma.domain.Monitoring;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "monitoring")
public class Monitoring implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "start_date", nullable = false)
    private Date start;

    @Column(name = "finish_date", nullable = false)
    private Date finish;

    @Column(name = "average_grade", nullable = true)
    private double averageGrade;

    @Column(name = "course_grade", nullable = true)
    private double courseGrade;

}