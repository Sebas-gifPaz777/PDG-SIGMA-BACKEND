package com.pdg.sigma.dto;

import lombok.Data;

@Data
public class UpdateSelectionStatusRequest {
    private Long monitoringId;
    private String monitorCode;
    private String newStatus;
}