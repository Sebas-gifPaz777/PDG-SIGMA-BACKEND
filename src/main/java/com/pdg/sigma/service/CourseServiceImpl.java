package com.pdg.sigma.service;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.dto.CourseDTO;
import com.pdg.sigma.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements  CourseService{

    @Autowired
    CourseRepository courseRepository;

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

    public List<CourseDTO> findByProgram(CourseDTO courseDto) {
        List<Course> list = courseRepository.findAll();
        List<CourseDTO> newList = new ArrayList<>();
        for(Course course: list){
            if(course.getProgram().getName().equalsIgnoreCase(courseDto.getName()))
                newList.add(new CourseDTO(course.getName()));

        }
        return newList;
    }
}
