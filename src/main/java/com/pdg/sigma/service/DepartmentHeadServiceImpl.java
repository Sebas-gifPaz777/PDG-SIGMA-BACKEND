package com.pdg.sigma.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.pdg.sigma.domain.*;
import com.pdg.sigma.dto.DepartmentHeadDTO;
import com.pdg.sigma.repository.CourseRepository;
import com.pdg.sigma.repository.HeadProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdg.sigma.repository.DepartmentHeadRepository;
import com.pdg.sigma.repository.CourseProfessorRepository;

@Service
public class DepartmentHeadServiceImpl implements DepartmentHeadService {

    @Autowired
    private DepartmentHeadRepository departmentHeadRepository;

    @Autowired
    private HeadProgramRepository headProgramRepository;

    @Autowired
    private CourseProfessorRepository courseProfessorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<DepartmentHead> findAll() {
        return departmentHeadRepository.findAll();
    }

    @Override
    public Optional<DepartmentHead> findById(Integer id) {
        return departmentHeadRepository.findById(id.toString());
    }

    @Override
    public DepartmentHead save(DepartmentHead departmentHead) {
        return departmentHeadRepository.save(departmentHead);
    }

    @Override
    public void deleteById(Integer id) {
        departmentHeadRepository.deleteById(id.toString());
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

    @Override
    public DepartmentHeadDTO getProfile(String id)throws Exception{
        Optional<DepartmentHead> departmentHead = departmentHeadRepository.findById(id);
        if(departmentHead.isPresent()){
            List<HeadProgram> list = headProgramRepository.findByDepartmentHeadId(id); //HeadProfessor is table between department head and program who it's from
            HeadProgram headProfessor= list.get(0);
            Program program = headProfessor.getProgram();
            School school = program.getSchool();

            return new DepartmentHeadDTO(school.getName(), program.getName(),"Jefe de Departamento", departmentHead.get().getName());
        }
        else
            throw new Exception("No existe jefe con este id");

    }
    
    @Override
    public List<Professor> getProfessorsByDepartmentHead(String departmentHeadId) {
        List<HeadProgram> headPrograms = headProgramRepository.findByDepartmentHeadId(departmentHeadId);

        if (headPrograms.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> programIds = headPrograms.stream()
                .map(headProgram -> headProgram.getProgram().getId())
                .collect(Collectors.toList());

        List<Course> courses = courseRepository.findByProgramIdIn(programIds);

        if (courses.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> courseIds = courses.stream()
                .map(Course::getId)
                .collect(Collectors.toList());

        return courseProfessorRepository.findProfessorsByCourseIds(courseIds); //professors
    }

    @Override
    public List<HeadProgram> getProgramsByDepartmentHead(String departmentHeadId) {
        return headProgramRepository.findByDepartmentHeadId(departmentHeadId);
    }
}
