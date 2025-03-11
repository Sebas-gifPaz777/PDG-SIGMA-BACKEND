package com.pdg.sigma.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student_course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Integer courseId;

    @Column(name = "student_id", nullable = false, length = 20)
    private String studentId;
}

// Monitoría has categories. 
// Y una de esas, siempre quemada ("Tipo monitoría")
// Solo se trae los estudiantes si es tipo monitoría