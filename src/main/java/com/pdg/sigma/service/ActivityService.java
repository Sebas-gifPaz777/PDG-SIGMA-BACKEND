package com.pdg.sigma.service;

import java.util.List;

import com.pdg.sigma.domain.Activity;
import com.pdg.sigma.dto.ActivityDTO;
import com.pdg.sigma.dto.ActivityRequestDTO;


public interface ActivityService extends GenericService<Activity,Integer>{
    public ActivityDTO update(ActivityRequestDTO updatedActivity) throws Exception;
    public List<ActivityDTO> findAll(String userId, String role) throws Exception;
}


