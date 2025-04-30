package com.pdg.sigma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportDTO {

    private String name;
    private Integer completed;
    private Integer late;
    private Integer pending;
    private String course;
    private String semester;
    private String program;
    private String professor;
    private String nameAndCourse;


    public ReportDTO(Integer completed, Integer late, Integer pending) {
        this.completed = completed;
        this.late = late;
        this.pending = pending;
    }
}


