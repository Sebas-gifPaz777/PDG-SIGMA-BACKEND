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
@Table(name = "attendance") 
public class Attendance {

    @Id
    @Column(name = "id")  
    private Integer id;  

    @Column(name = "activity_id", nullable = false)
    private Integer activityId;

    @Column(name = "student_id", nullable = false)
    private String studentId;
}
