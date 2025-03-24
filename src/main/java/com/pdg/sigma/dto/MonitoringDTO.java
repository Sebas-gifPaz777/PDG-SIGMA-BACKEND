package com.pdg.sigma.dto;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.Professor;
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
    private Professor professor;

    private String programName;
    private String courseName;
    private String schoolName;


    private Date start;
    private Date finish;
    private double averageGrade;
    private double courseGrade;
    private String  professorId;
    private String semester;
    private String monitor;
    public MonitoringDTO(Long id, Program program, Course course, School school, String programName, String courseName, String schoolName, Date start, Date finish, double averageGrade, double courseGrade, String professorId, String semester) {
        this.id = id;
        this.program = program;
        this.course = course;
        this.school = school;
        this.programName = programName;
        this.courseName = courseName;
        this.schoolName = schoolName;
        this.start = start;
        this.finish = finish;
        this.averageGrade = 4.5;
        this.courseGrade = 4.5;
        this.professorId = professorId;
        this.semester = semester;
    }

    public MonitoringDTO(Long id, Program program, Course course, School school, Date start, Date finish, double averageGrade, double courseGrade){
        this.id = id;
        this.program = program;
        this.course = course;
        this.school = school;
        this.start = start;
        this.finish = finish;
        this.averageGrade = 4.5;
        this.courseGrade = 4.5;
    }

    public MonitoringDTO(Long id, Course course, School school,Date start, Date finish, double averageGrade, double courseGrade){
        this.id = id;
        this.course = course;
        this.school = school;
        this.start = start;
        this.finish = finish;
        this.averageGrade = 4.5;
        this.courseGrade = 4.5;
    }

    public MonitoringDTO(String programName, String courseName, String schoolName, Date start, Date finish, double averageGrade, double courseGrade, String professorId ){
        this.programName = programName;
        this.courseName = courseName;
        this.schoolName = schoolName;
        this.start = start;
        this.finish = finish;
        this.averageGrade = 4.5;
        this.courseGrade = 4.5;
        this.professorId = professorId;
    }

    public MonitoringDTO(String programName, String courseName, String schoolName, Date start, Date finish, String professorId, String semester ){
        this.programName = programName;
        this.courseName = courseName;
        this.schoolName = schoolName;
        this.start = start;
        this.finish = finish;
        this.averageGrade = 4.5;
        this.courseGrade = 4.5;
        this.professorId = professorId;
        this.semester = semester;
    }
    public MonitoringDTO(String programName, String courseName){
        this.programName = programName;
        this.courseName = courseName;
    }

    public MonitoringDTO(double averageGrade, double courseGrade){
        this.averageGrade = averageGrade;
        this.courseGrade = courseGrade;
    }

    //Used for get Monitor and Professor profile
    public MonitoringDTO(Long id, String courseName, Date start, Date finish, String semester, String monitor) {
        this.id = id;
        this.courseName = courseName;
        this.start = start;
        this.finish = finish;
        this.semester = semester;
        this.monitor = monitor;
    }
}
