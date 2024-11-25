package com.pdg.sigma.dto;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.Program;
import com.pdg.sigma.domain.School;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@NoArgsConstructor
public class MonitoringDTO  implements Serializable {
    private Long id;
    private Program program;
    private Course course;
    private School school;

    private String programName;
    private String courseName;
    private String schoolName;

    private Date start;
    private Date finish;
    private double averageGrade;
    private double courseGrade;

    public MonitoringDTO(Long id, Program program, Course course, School school, String programName, String courseName, String schoolName, Date start, Date finish, double averageGrade, double courseGrade) {
        this.id = id;
        this.program = program;
        this.course = course;
        this.school = school;
        this.programName = programName;
        this.courseName = courseName;
        this.schoolName = schoolName;
        this.start = start;
        this.finish = finish;
        this.averageGrade = averageGrade;
        this.courseGrade = courseGrade;
    }

    public MonitoringDTO(Long id, Program program, Course course, School school, Date start, Date finish, double averageGrade, double courseGrade){
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

    public MonitoringDTO(String programName, String courseName, String schoolName, Date start, Date finish, double averageGrade, double courseGrade){
        this.programName = programName;
        this.courseName = courseName;
        this.schoolName = schoolName;
        this.start = start;
        this.finish = finish;
        this.averageGrade = averageGrade;
        this.courseGrade = courseGrade;
    }

}
