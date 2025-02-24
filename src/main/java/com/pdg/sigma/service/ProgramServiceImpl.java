package com.pdg.sigma.service;

import com.pdg.sigma.domain.Program;
import com.pdg.sigma.dto.ProgramDTO;
import com.pdg.sigma.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProgramServiceImpl implements ProgramService {

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private SchoolService schoolService;

    @Override
    public List<ProgramDTO> findAll() {
        List<Program> list = programRepository.findAll();
        List<ProgramDTO> newList = new ArrayList<>();

        for(Program program:list){
            newList.add(new ProgramDTO(program.getName()));
        }

        return newList;
    }

    @Override
    public Optional<ProgramDTO> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public ProgramDTO save(ProgramDTO entity) throws Exception {
        return null;
    }

    @Override
    public ProgramDTO update(ProgramDTO entity) throws Exception {
        return null;
    }

    @Override
    public void delete(ProgramDTO entity) throws Exception {

    }

    @Override
    public void deleteById(Long aLong) throws Exception {

    }

    @Override
    public void validate(ProgramDTO entity) throws Exception {

    }

    @Override
    public Long count() {
        return null;
    }

    public List<ProgramDTO> findBySchoolName(ProgramDTO programDto) {
        List<Program> list = programRepository.findAll();
        List<ProgramDTO> newList = new ArrayList<>();
        for(Program program: list){
            if (program.getSchool().getName().equalsIgnoreCase(programDto.getName())){
                newList.add(new ProgramDTO(program.getName()));
            }
        }

        return newList;
    }
}
