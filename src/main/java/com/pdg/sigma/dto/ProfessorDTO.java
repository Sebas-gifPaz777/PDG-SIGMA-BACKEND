package com.pdg.sigma.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfessorDTO {

    private String school;
    private String program;
    private String rol;
    private String name;

    public ProfessorDTO(String school, String program, String role, String name){
        this.school = school;
        this.program = program;
        this.rol = role;
        this.name = name;
    }
}
