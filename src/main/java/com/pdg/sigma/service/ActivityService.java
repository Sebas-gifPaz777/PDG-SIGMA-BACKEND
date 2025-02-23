package com.pdg.sigma.service;

import com.pdg.sigma.domain.Activity;
import com.pdg.sigma.dto.ActivityDTO;

import java.util.EmptyStackException;
import java.util.List;

public interface ActivityService extends GenericService<ActivityDTO,Integer>{

    public List<ActivityDTO> findAll(String userId, String role) throws Exception;
}


