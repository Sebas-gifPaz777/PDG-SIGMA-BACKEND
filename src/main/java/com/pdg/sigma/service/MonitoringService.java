package com.pdg.sigma.service;

import com.pdg.sigma.domain.Monitoring;
import com.pdg.sigma.dto.MonitoringDTO;

import java.util.List;

public interface MonitoringService extends GenericService<Monitoring, Long>{
    public Monitoring save(MonitoringDTO monitoringDTO) throws Exception;


}
