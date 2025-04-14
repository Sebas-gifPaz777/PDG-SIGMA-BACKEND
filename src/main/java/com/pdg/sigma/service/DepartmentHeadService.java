package com.pdg.sigma.service;

import java.util.List;

import com.pdg.sigma.domain.DepartmentHead;
import com.pdg.sigma.domain.HeadProgram;
import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.dto.DepartmentHeadDTO;

public interface DepartmentHeadService extends GenericService<DepartmentHead, Integer> {

    DepartmentHeadDTO getProfile(String departmentHeadId) throws Exception;
    List<Professor> getProfessorsByDepartmentHead(String departmentHeadId);
    List<HeadProgram> getProgramsByDepartmentHead(String departmentHeadId);

}
