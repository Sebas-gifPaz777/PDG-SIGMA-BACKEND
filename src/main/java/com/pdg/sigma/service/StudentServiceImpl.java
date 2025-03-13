package com.pdg.sigma.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdg.sigma.domain.Student;
import com.pdg.sigma.domain.StudentCourse;
import com.pdg.sigma.repository.StudentCourseRepository;
import com.pdg.sigma.repository.StudentRepository;


@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private StudentCourseRepository studentCourseRepository;

    public List<StudentCourse> getStudentsByCourse(Integer courseId) {
        return studentCourseRepository.findByCourseId(courseId);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> findById(String id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student save(Student entity) {
        return studentRepository.save(entity);
    }

    @Override
    public Student update(Student entity) {
        return studentRepository.save(entity);
    }

    @Override
    public void delete(Student entity) {
        studentRepository.delete(entity);
    }

    @Override
    public void deleteById(String id) {
        studentRepository.deleteById(id);
    }

    @Override
    public void validate(Student entity) {
        
    }

    @Override
    public Long count() {
        return studentRepository.count();
    }
}
