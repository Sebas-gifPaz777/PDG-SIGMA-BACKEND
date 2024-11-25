package com.pdg.sigma.dto;

import com.pdg.sigma.domain.Monitoring;

import java.io.Serializable;

public class CandidatureDTO implements Serializable {
    private String code;
    private String name;
    private String lastName;
    private int semester;
    private double gradeAverage;
    private double gradeCourse;
    private String email;
    private Monitoring monitoring;

    public CandidatureDTO(String code, String name, String lastName, int semester, double gradeAverage, double gradeCourse, Monitoring monitoring, String email){
        this.code = code;
        this.name = name;
        this.lastName= lastName ;
        this.semester= semester;
        this.gradeAverage= gradeAverage;
        this.gradeCourse= gradeCourse;
        this.email= email;
        this.monitoring = monitoring;
    }

    public CandidatureDTO(String code, String name, String lastName, int semester, double gradeCourse, String email, Monitoring monitoring){
        this.code = code;
        this.name = name;
        this.lastName= lastName ;
        this.semester= semester;
        this.gradeAverage= 0.0;
        this.gradeCourse= gradeCourse;
        this.email= email;
        this.monitoring = monitoring;
    }

    public CandidatureDTO(String code, String name, String lastName, int semester, double gradeAverage, Monitoring monitoring, String email){
        this.code = code;
        this.name = name;
        this.lastName= lastName ;
        this.semester= semester;
        this.gradeAverage= gradeAverage;
        this.gradeCourse= 0.0;
        this.email= email;
        this.monitoring = monitoring;
    }


}
