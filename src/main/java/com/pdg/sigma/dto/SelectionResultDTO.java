package com.pdg.sigma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SelectionResultDTO {
    private String code;
    private Long idMonitoring;
    private String estadoSeleccion; // valores: "seleccionado", "no seleccionado"
}
