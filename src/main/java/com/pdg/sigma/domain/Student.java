package com.pdg.sigma.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student") 
public class Student {

    @Id
    @Column(name = "code")  
    private String code;  

    @Column(name = "name", nullable = false)
    private String name;
}
