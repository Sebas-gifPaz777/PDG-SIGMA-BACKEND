package com.pdg.sigma.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "professor")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(100)")
    private String name;

    @Column(name = "password", nullable = false)
    private String password;
}