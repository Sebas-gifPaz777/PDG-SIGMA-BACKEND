package com.pdg.sigma.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class SchoolDTO {
    private Long id;
    private String name;

    public SchoolDTO(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public SchoolDTO(String name){
        this.name = name;
    }
}
