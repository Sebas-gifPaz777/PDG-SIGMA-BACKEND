package com.pdg.sigma.service;

import com.pdg.sigma.domain.Candidature;
import com.pdg.sigma.domain.Monitoring;
import com.pdg.sigma.dto.MonitoringDTO;

public interface MonitoringService extends GenericService<Monitoring, Long>{
    public Monitoring save(MonitoringDTO monitoringDTO) throws Exception;
}
