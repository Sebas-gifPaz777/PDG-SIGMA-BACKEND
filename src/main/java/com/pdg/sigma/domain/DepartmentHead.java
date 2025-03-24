package com.pdg.sigma.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "department_head")
public class DepartmentHead {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "password",nullable = false)
    private String password;
}
