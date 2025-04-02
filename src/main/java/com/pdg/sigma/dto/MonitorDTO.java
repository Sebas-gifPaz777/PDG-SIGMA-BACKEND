package com.pdg.sigma.dto;

import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.domain.Monitoring;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
public class MonitorDTO implements Serializable {
    private String code;
    private String name;
    private String lastName;
    private int semester;
    private double gradeAverage;
    private double gradeCourse;
    private String email;
    private Monitoring monitoring;
    private String userId;
    private String monitoringId;
    private String course;

    //Attributes for profile
    private String school;
    private String program;
    private String rol;


    public MonitorDTO(String code, String name, String lastName, int semester, double gradeAverage, double gradeCourse, Monitoring monitoring, String email, String userId, String monitoringId){
        this.code = code;
        this.name = name;
        this.lastName= lastName ;
        this.semester= semester;
        this.gradeAverage= gradeAverage;
        this.gradeCourse= gradeCourse;
        this.email= email;
        this.monitoring = monitoring;
        this.userId = userId;
        this.monitoringId = monitoringId;
    }

    public MonitorDTO(String code, String name, String lastName, int semester, double gradeCourse, String email, Monitoring monitoring){
        this.code = code;
        this.name = name;
        this.lastName= lastName ;
        this.semester= semester;
        this.gradeAverage= 0.0;
        this.gradeCourse= gradeCourse;
        this.email= email;
        this.monitoring = monitoring;
    }

    public MonitorDTO(String code, String name, String lastName, int semester, double gradeAverage, Monitoring monitoring, String email){
        this.code = code;
        this.name = name;
        this.lastName= lastName ;
        this.semester= semester;
        this.gradeAverage= gradeAverage;
        this.gradeCourse= 0.0;
        this.email= email;
        this.monitoring = monitoring;
    }

    public MonitorDTO(String userId, String monitoringId){
        this.userId = userId;
        this.monitoringId = monitoringId;
    }

    public MonitorDTO(Monitor applicant){
        this.code = applicant.getCode();
        this.name = applicant.getName();
        this.lastName = applicant.getLastName();
        this.semester = applicant.getSemester();
        this.gradeAverage = applicant.getGradeAverage();
        this.gradeCourse = applicant.getGradeCourse();
        this.email = applicant.getEmail();
    }

    //Use in profile
    public MonitorDTO(String school, String program, String role, String name){
        this.school = school;
        this.program = program;
        this.rol = role;
        this.name = name;
    }

    //use to get monitor per monitoring


    public MonitorDTO(String name, String userId, String rol) {
        this.name = name;
        this.userId = userId;
        this.rol = rol;
    }
}
