package com.pdg.sigma.dto;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ActivityRequestDTO {

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
    private Integer monitoringId;

    private Integer professorId;
    private String monitorId;


    @NotNull
    private String state;

    private String semester;

    private Date delivey;
}
