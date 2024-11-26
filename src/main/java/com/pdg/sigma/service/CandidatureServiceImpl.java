package com.pdg.sigma.service;

import com.pdg.sigma.domain.Candidature;
import com.pdg.sigma.domain.Monitoring;
import com.pdg.sigma.domain.Student;
import com.pdg.sigma.dto.CandidatureDTO;
import com.pdg.sigma.repository.CandidatureRepository;
import com.pdg.sigma.repository.MonitoringRepository;
import com.pdg.sigma.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidatureServiceImpl implements CandidatureService{

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private MonitoringRepository monitoringRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Candidature> findAll() {
        return candidatureRepository.findAll();
    }

    @Override
    public Optional<Candidature> findById(String s) {
        return Optional.empty();
    }

    @Override
    public Candidature save(Candidature entity) throws Exception {


        return null;
    }

    public Candidature saveNew(CandidatureDTO candidatureDTO) throws Exception{
        Student student = studentRepository.findById(candidatureDTO.getUserId()).get();
        Monitoring monitoring = monitoringRepository.findById(Long.parseLong(candidatureDTO.getMonitoringId())).get();
        Optional<Candidature> posible = candidatureRepository.findByCode(student.getCode());

        if(posible.isPresent() && posible.get().getMonitoring().equals(monitoring)) {
            throw new Exception("Ya existe una postulacion a este nombre");
        }
        if(student.getGradeAverage()<monitoring.getAverageGrade() || student.getGradeCourse()< monitoring.getCourseGrade()){
            throw new Exception("No cumple con los requisitos suficientes para aplicar a la vacante");
        }
        Candidature candidature = new Candidature(student.getCode(), student.getName(), student.getLastName(), student.getSemester(), student.getGradeAverage(), student
                .getGradeCourse(), student.getEmail(), monitoring);

        return candidatureRepository.save(candidature);
    }

    @Override
    public Candidature update(Candidature entity) throws Exception {
        return null;
    }

    @Override
    public void delete(Candidature entity) throws Exception {

    }

    @Override
    public void deleteById(String s) throws Exception {

    }

    @Override
    public void validate(Candidature entity) throws Exception {

    }

    @Override
    public Long count() {
        return null;
    }
}
