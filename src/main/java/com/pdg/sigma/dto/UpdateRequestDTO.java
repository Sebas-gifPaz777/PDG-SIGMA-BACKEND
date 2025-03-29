package com.pdg.sigma.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor 
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateRequestDTO {
    private String professorId;
    private String departmentHeadId;
    private String updateType; // "sameSemester" o "newSemester"
    private boolean removeMonitors; // para "sameSemester"
}

