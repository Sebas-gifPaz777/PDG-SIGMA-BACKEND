package com.pdg.sigma.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewAcitivityRequestDTO {
    private Integer id;

    private Date creation;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    private Date finish;

    @NotNull
    private String roleCreator;

    @NotNull
    private String roleResponsable;

    private String category;

    @NotNull
    @Size(max = 255)
    private String description;

    @NotNull
    private int monitoringId;

    private String professorId;
    private String monitorId;


    @NotNull
    private String state;

    private String semester;

    private Date delivey;
}
