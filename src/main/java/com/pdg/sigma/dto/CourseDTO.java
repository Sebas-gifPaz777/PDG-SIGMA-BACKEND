package com.pdg.sigma.dto;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.Program;
import lombok.Data;

import java.io.Serializable;
@Data
public class CourseDTO  implements Serializable {
    private Long id;
    private String name;
    private Program program;

    public CourseDTO(Long id, String name, Program program){
        this.id = id;
        this.name = name;
        this.program= program;
    }

    public CourseDTO(String name){
        this.name = name;
    }
}
