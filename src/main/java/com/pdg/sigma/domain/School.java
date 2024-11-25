package com.pdg.sigma.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "school")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(100)")
    private String name;


}
