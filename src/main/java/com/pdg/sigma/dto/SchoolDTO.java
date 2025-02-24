package com.pdg.sigma.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;
@Data
public class SchoolDTO implements Serializable {
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
