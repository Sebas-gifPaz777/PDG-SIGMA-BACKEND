package com.pdg.sigma.service;

import com.pdg.sigma.domain.*;
import com.pdg.sigma.dto.MonitoringDTO;
import com.pdg.sigma.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private MonitoringProfessorRepository monitoringProfessorRepository;

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
        Monitoring newMonitoring = null;
        Optional<Professor> professor = null;
        if(program.getName().equals(entity.getProgramName()))
            if(school.getName().equals(entity.getSchoolName()))
                if(course.getName().equals(entity.getCourseName()))
                    if(monitoringRepository.findByCourse(course).isEmpty()){

                        newMonitoring = new Monitoring(school,program,course,entity.getStart(),entity.getFinish(), 4.5, 4.5, entity.getSemester());
                        monitoringRepository.save(newMonitoring);
                        professor = professorRepository.findById(entity.getProfessorId());
                        if(professor.isPresent())
                            monitoringProfessorRepository.save(new MonitoringProfessor(newMonitoring,professor.get()));
                        else
                            throw new Exception("El profesor no está registrado con esta Id");

                        return monitoringRepository.findByCourse(course).get();
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
    public List<Monitoring> findBySchool(MonitoringDTO monitoringDTO){ //programName = nombre elemento a buscar, courseName = state o estado
        School entity = schoolRepository.findByName(monitoringDTO.getProgramName());
        List<Monitoring> monitoring = monitoringRepository.findBySchool(entity);
        Date currentDate = new Date();
        if(monitoringDTO.getCourseName().equalsIgnoreCase("Activo") || monitoringDTO.getCourseName().isBlank()){
            List<Monitoring> temp = new ArrayList<>();
            for(Monitoring element: monitoring){
                if(element.getStart().before(currentDate) && element.getFinish().after(currentDate)){
                    temp.add(element);
                }
            }
            return temp;



        }
        else if(monitoringDTO.getCourseName().equalsIgnoreCase("Inactivo")){
            List<Monitoring> temp = new ArrayList<>();
            for(Monitoring element: monitoring){
                if(element.getStart().after(currentDate) && element.getFinish().after(currentDate)){
                    temp.add(element);
                }
            }
            return temp;
        }



        List<Monitoring> temp = new ArrayList<>();
        for(Monitoring element: monitoring){
            if(element.getStart().before(currentDate) && element.getFinish().before(currentDate)){
                temp.add(element);
            }
        }
        return temp;
    }

    public List<Monitoring> findByProgram(MonitoringDTO monitoringDTO) {//programName = nombre elemento a buscar, courseName = state o estado
        Program entity = programRepository.findByName(monitoringDTO.getProgramName());
        List<Monitoring> monitoring = monitoringRepository.findByProgram(entity);
        Date currentDate = new Date();
        if(monitoringDTO.getCourseName().equalsIgnoreCase("Activo") || monitoringDTO.getCourseName().isBlank()){
            List<Monitoring> temp = new ArrayList<>();
            for(Monitoring element: monitoring){
                if(element.getStart().before(currentDate) && element.getFinish().after(currentDate)){
                    temp.add(element);
                }
            }
            return temp;
        }
        else if(monitoringDTO.getCourseName().equalsIgnoreCase("Inactivo")){
            List<Monitoring> temp = new ArrayList<>();
            for(Monitoring element: monitoring){
                if(element.getStart().after(currentDate) && element.getFinish().after(currentDate)){
                    temp.add(element);
                }
            }
            return temp;
        }

        List<Monitoring> temp = new ArrayList<>();
        for(Monitoring element: monitoring){
            if(element.getStart().before(currentDate) && element.getFinish().before(currentDate)){
                temp.add(element);
            }
        }
        return temp;
    }

    public List<Monitoring> findByCourse(MonitoringDTO monitoringDTO) {//programName = nombre elemento a buscar, courseName = state o estado
        Course entity = courseRepository.findByName(monitoringDTO.getProgramName());
        Optional<Monitoring> monitoring = monitoringRepository.findByCourse(entity);
        Date currentDate = new Date();
        if(monitoringDTO.getCourseName().equalsIgnoreCase("Activo") || monitoringDTO.getCourseName().isBlank()){
            List<Monitoring> temp = new ArrayList<>();
            for(Monitoring element: monitoring.stream().toList()){
                if(element.getStart().before(currentDate) && element.getFinish().after(currentDate)){
                    temp.add(element);
                }
            }
            return temp;
        }
        else if(monitoringDTO.getCourseName().equalsIgnoreCase("Inactivo")){
            List<Monitoring> temp = new ArrayList<>();
            for(Monitoring element: monitoring.stream().toList()){
                if(element.getStart().after(currentDate) && element.getFinish().after(currentDate)){
                    temp.add(element);
                }
            }
            return temp;
        }

        List<Monitoring> temp = new ArrayList<>();
        for(Monitoring element: monitoring.stream().toList()){
            if(element.getStart().before(currentDate) && element.getFinish().before(currentDate)){
                temp.add(element);
            }
        }
        return temp;
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
