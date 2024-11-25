package com.pdg.sigma.service;

import com.pdg.sigma.domain.Course;
import com.pdg.sigma.domain.Monitoring;
import com.pdg.sigma.domain.Program;
import com.pdg.sigma.domain.School;
import com.pdg.sigma.dto.MonitoringDTO;
import com.pdg.sigma.repository.CourseRepository;
import com.pdg.sigma.repository.MonitoringRepository;
import com.pdg.sigma.repository.ProgramRepository;
import com.pdg.sigma.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MonitoringServiceImpl implements MonitoringService{

    @Autowired
    private MonitoringRepository monitoringRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProgramRepository programRepository;


    @Override
    public List<Monitoring> findAll() {
        return monitoringRepository.findAll();
    }

    @Override
    public Optional<Monitoring> findById(Long aLong) {
        return monitoringRepository.findById(aLong);
    }

    @Override
    public Monitoring save(Monitoring entity) throws Exception {
        return null;
    }

    @Override
    public Monitoring save(MonitoringDTO entity) throws Exception{
        Program program = programRepository.findByName(entity.getProgramName());
        School school = schoolRepository.findByName(entity.getSchoolName());
        Course course = courseRepository.findByName(entity.getCourseName());

        if(program.getName().equals(entity.getProgramName()))
            if(school.getName().equals(entity.getSchoolName()))
                if(course.getName().equals(entity.getCourseName()))
                    if(monitoringRepository.findByCourse(course).isEmpty()){
                        return monitoringRepository.save(new Monitoring(school,program,course,entity.getStart(),entity.getFinish(), entity.getAverageGrade(), entity.getCourseGrade()));

                    }
                    else
                        throw new Exception("Ya existe una monitoria para esta materia");
                else
                    throw new Exception("No existe un curso con este nombre");
            else
                throw new Exception("No existe un programa con este nombre");
        else
            throw new Exception("No existe una facultad con este nombre");
    }

    @Override
    public Monitoring update(Monitoring entity) throws Exception {
        return null;
    }

    @Override
    public void delete(Monitoring entity) throws Exception {

    }

    @Override
    public void deleteById(Long aLong) throws Exception {

    }

    @Override
    public void validate(Monitoring entity) throws Exception {

    }

    @Override
    public Long count() {
        return null;
    }
}
