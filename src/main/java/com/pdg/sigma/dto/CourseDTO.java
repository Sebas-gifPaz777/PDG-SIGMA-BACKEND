package com.pdg.sigma.dto;

import com.pdg.sigma.domain.Program;

public class CourseDTO {
    private Long id;
    private String name;
    private Program program;

    public CourseDTO(String name){
        this.name = name;
    }
}
