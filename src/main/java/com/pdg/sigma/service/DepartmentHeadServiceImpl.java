package com.pdg.sigma.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdg.sigma.domain.DepartmentHead;
import com.pdg.sigma.repository.DepartmentHeadRepository;

@Service
public class DepartmentHeadServiceImpl implements DepartmentHeadService {

    @Autowired
    private DepartmentHeadRepository departmentHeadRepository;

    @Override
    public List<DepartmentHead> findAll() {
        return departmentHeadRepository.findAll();
    }

    @Override
    public Optional<DepartmentHead> findById(Integer id) {
        return departmentHeadRepository.findById(id);
    }

    @Override
    public DepartmentHead save(DepartmentHead departmentHead) {
        return departmentHeadRepository.save(departmentHead);
    }

    @Override
    public void deleteById(Integer id) {
        departmentHeadRepository.deleteById(id);
    }

    @Override
    public DepartmentHead update(DepartmentHead entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(DepartmentHead entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void validate(DepartmentHead entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long count() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
