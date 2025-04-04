package com.pdg.sigma.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.CourseProfessor;
import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.dto.CourseDTO;
import com.pdg.sigma.repository.CourseRepository;
import com.pdg.sigma.repository.CourseProfessorRepository;

@Service
public class CourseServiceImpl implements  CourseService{

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CourseProfessorRepository courseProfessorRepository;

    @Override
    public List<CourseDTO> findAll() {
        List<Course> list = courseRepository.findAll();
        List<CourseDTO> newList = new ArrayList<>();

        for(Course course:list){
            newList.add(new CourseDTO(course.getName()));
        }

        return newList;
    }

    @Override
    public Optional<CourseDTO> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public CourseDTO save(CourseDTO entity) throws Exception {
        return null;
    }

    @Override
    public CourseDTO update(CourseDTO entity) throws Exception {
        return null;
    }

    @Override
    public void delete(CourseDTO entity) throws Exception {

    }

    @Override
    public void deleteById(Long aLong) throws Exception {

    }

    @Override
    public void validate(CourseDTO entity) throws Exception {

    }

    @Override
    public Long count() {
        return null;
    }

    @Override
    public Optional<Course> findEntityById(Long id) {
        return courseRepository.findById(id);
    }    

    public List<CourseDTO> findByProgram(CourseDTO courseDto) {
        List<Course> list = courseRepository.findAll();
        List<CourseDTO> newList = new ArrayList<>();
        for(Course course: list){
            if(course.getProgram().getName().equalsIgnoreCase(courseDto.getName()))
                newList.add(new CourseDTO(course.getName()));

        }
        return newList;
    }

    public List<Course> findByProgramIds(List<Long> programIds) {
        List<Course> list = courseRepository.findByProgramIdIn(programIds);
        return list;
    }

    public List<Course> findByProgramId(Long programId) {
        List<Course> list = courseRepository.findByProgramId(programId);
        return list;
    }

    public List<Course> getCoursesByProfessorId(String professorId) {
        List<CourseProfessor> courseProfessors = courseProfessorRepository.findByProfessor(new Professor(professorId));
        
        return courseProfessors.stream()
                .map(CourseProfessor::getCourse)
                .collect(Collectors.toList());
    }


}
