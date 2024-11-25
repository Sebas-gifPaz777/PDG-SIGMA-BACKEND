package com.pdg.sigma.dto;

import com.pdg.sigma.domain.School;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProgramDTO implements Serializable {
    private Long id;
    private String name;
    private School school;

    public ProgramDTO(Long id, String name, School school){
        this.name = name;
        this.id = id;
        this.school = school;
    }

    public ProgramDTO(String name){
        this.name = name;
    }
}