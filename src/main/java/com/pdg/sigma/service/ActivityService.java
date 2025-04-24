package com.pdg.sigma.service;

import java.util.List;

import com.pdg.sigma.domain.Activity;
import com.pdg.sigma.dto.ActivityDTO;
import com.pdg.sigma.dto.ActivityRequestDTO;
import com.pdg.sigma.dto.NewActivityRequestDTO;

public interface ActivityService extends GenericService<Activity, Integer> {
    public ActivityDTO update(ActivityRequestDTO updatedActivity) throws Exception;
    public List<ActivityDTO> findAll(String userId, String role) throws Exception;
    public ActivityDTO save(NewActivityRequestDTO dto) throws Exception;
    
    public boolean updateState(String id) throws Exception;
    
}