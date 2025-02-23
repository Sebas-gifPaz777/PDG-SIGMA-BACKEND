package com.pdg.sigma.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToOne
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

    @Column(name = "semester", nullable = false)
    private String semester;

    @OneToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    public Monitoring(School school, Program program, Course course,
                      Date start, Date finish, double averageGrade, double courseGrade, String semester, Professor professor) {
        this.school = school;
        this.program = program;
        this.course = course;
        this.start = start;
        this.finish = finish;
        this.averageGrade = averageGrade;
        this.courseGrade = courseGrade;
        this.semester = semester;
        this.professor =  professor;
    }

    public Monitoring() {

    }
}