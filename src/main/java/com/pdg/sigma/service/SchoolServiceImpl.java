package com.pdg.sigma.service;

import com.pdg.sigma.domain.School;
import com.pdg.sigma.dto.SchoolDTO;
import com.pdg.sigma.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SchoolServiceImpl implements SchoolService{

    @Autowired
    private SchoolRepository schoolRepository;

    @Override
    public List<SchoolDTO> findAll() {
        List<School> list = schoolRepository.findAll();
        List<SchoolDTO> newList = new ArrayList<>();

        for(School school:list){
            newList.add(new SchoolDTO(school.getName()));
        }

        return newList;

    }

    @Override
    public Optional<SchoolDTO> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public SchoolDTO save(SchoolDTO entity) throws Exception {
        return null;
    }

    @Override
    public SchoolDTO update(SchoolDTO entity) throws Exception {
        return null;
    }

    @Override
    public void delete(SchoolDTO entity) throws Exception {

    }

    @Override
    public void deleteById(Long aLong) throws Exception {

    }

    @Override
    public void validate(SchoolDTO entity) throws Exception {

    }

    @Override
    public Long count() {
        return null;
    }
}
