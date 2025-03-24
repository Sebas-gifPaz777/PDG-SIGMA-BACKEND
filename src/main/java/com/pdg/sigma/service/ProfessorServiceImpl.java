package com.pdg.sigma.service;

import com.pdg.sigma.domain.*;
import com.pdg.sigma.dto.MonitorDTO;
import com.pdg.sigma.dto.ProfessorDTO;
import com.pdg.sigma.repository.CourseProfessorRepository;
import com.pdg.sigma.repository.MonitoringMonitorRepository;
import com.pdg.sigma.repository.MonitoringRepository;
import com.pdg.sigma.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessorServiceImpl implements ProfessorService{

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    MonitoringRepository monitoringRepository;

    @Autowired
    CourseProfessorRepository courseProfessorRepository;

    @Override
    public List<ProfessorDTO> findAll() {
        return null;
    }

    @Override
    public Optional<ProfessorDTO> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public ProfessorDTO save(ProfessorDTO entity) throws Exception {
        return null;
    }

    @Override
    public ProfessorDTO update(ProfessorDTO entity) throws Exception {
        return null;
    }

    @Override
    public void delete(ProfessorDTO entity) throws Exception {

    }

    @Override
    public void deleteById(Long aLong) throws Exception {

    }

    @Override
    public void validate(ProfessorDTO entity) throws Exception {

    }

    @Override
    public Long count() {
        return null;
    }

    public ProfessorDTO getProfile(String id) throws Exception{
        Optional<Professor> professor = professorRepository.findById(id);
        String school="";
        String program="";
        String role="Profesor";
        if(professor.isPresent()){

            List<CourseProfessor> list = courseProfessorRepository.findByProfessor(professor.get());

            if(!list.isEmpty()){

                List<String> schools = new ArrayList<>();
                List<String> programs = new ArrayList<>();
                for(int i=0; i<list.size();i++){

                    if(!schools.contains(list.get(i).getCourse().getProgram().getSchool().getName())){
                        if(i!= list.size()-1){
                            school = school+list.get(i).getCourse().getProgram().getSchool().getName()+" | ";
                        }
                        else{
                            school = school+list.get(i).getCourse().getProgram().getSchool().getName();
                        }
                        schools.add(list.get(i).getCourse().getProgram().getSchool().getName());
                    }
                    if(!programs.contains(list.get(i).getCourse().getProgram().getName())){
                        if(i!= list.size()-1){
                            program = program+list.get(i).getCourse().getProgram().getName()+" | ";
                        }
                        else{
                            program = program+list.get(i).getCourse().getProgram().getName();
                        }
                        programs.add(list.get(i).getCourse().getProgram().getName());
                    }

                }
                ProfessorDTO data = new ProfessorDTO(school,program,role, professor.get().getName());

                return data;
            }
            else
                throw new Exception("No tiene asignado cursos para este semestre");


        }
        else
            throw new Exception("No existe profesor con este ID");

    }
}
