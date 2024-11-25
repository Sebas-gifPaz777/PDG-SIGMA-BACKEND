package com.pdg.sigma.dto;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.Program;
import com.pdg.sigma.domain.School;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Date;

public class MonitoringDTO {
    private Long id;
    private String name;
    private Program program;
    private Course course;

    private School school;
    private Date start;
    private Date finish;
    private double averageGrade;
    private double courseGrade;

    public MonitoringDTO(Long id, Program program, Course course, School school,Date start, Date finish, double averageGrade, double courseGrade){
        this.id = id;
        this.program = program;
        this.course = course;
        this.school = school;
        this.start = start;
        this.finish = finish;
        this.averageGrade = averageGrade;
        this.courseGrade = courseGrade;
    }

    public MonitoringDTO(Long id, Course course, School school,Date start, Date finish, double averageGrade, double courseGrade){
        this.id = id;
        this.course = course;
        this.school = school;
        this.start = start;
        this.finish = finish;
        this.averageGrade = averageGrade;
        this.courseGrade = courseGrade;
    }


}
