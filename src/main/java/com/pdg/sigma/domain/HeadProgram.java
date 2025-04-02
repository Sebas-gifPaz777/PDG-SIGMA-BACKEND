package com.pdg.sigma.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "head_professor")
@Getter
@Setter
public class HeadProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "department_head_id", nullable = false)
    private DepartmentHead departmentHead;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;
}
