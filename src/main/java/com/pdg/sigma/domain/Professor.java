package com.pdg.sigma.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "professor")
public class Professor {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "varchar(255)")
    private String id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(100)")
    private String name;

    @Column(name = "password", nullable = false)
    private String password;
}