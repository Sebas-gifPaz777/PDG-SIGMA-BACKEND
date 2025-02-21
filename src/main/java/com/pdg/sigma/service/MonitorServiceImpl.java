package com.pdg.sigma.service;

import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.domain.Monitoring;
import com.pdg.sigma.domain.MonitoringMonitor;
import com.pdg.sigma.domain.Prospect;
import com.pdg.sigma.dto.MonitorDTO;
import com.pdg.sigma.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MonitorServiceImpl implements MonitorService {

    @Autowired
    private MonitorRepository monitorRepository;

    @Autowired
    private MonitoringRepository monitoringRepository;

    @Autowired
    private ProspectRepository prospectRepository;

    @Autowired
    private MonitoringMonitorRepository monitoringMonitorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Monitor> findAll() {
        return monitorRepository.findAll();
    }

    @Override
    public Optional<Monitor> findById(String s) {
        return Optional.empty();
    }

    @Override
    public Monitor save(Monitor entity) throws Exception {


        return null;
    }

    public Monitor saveNew(MonitorDTO monitorDTO) throws Exception{
        Prospect prospect = prospectRepository.findById(monitorDTO.getUserId()).get();
        Monitoring monitoring = monitoringRepository.findById(Long.parseLong(monitorDTO.getMonitoringId())).get();
        Optional<Monitor> posible = monitorRepository.findByCode(prospect.getCode());

        if(posible.isPresent()){
            Optional<MonitoringMonitor> network = monitoringMonitorRepository.findByMonitoringAndMonitor(monitoring,posible.get());
            if(network.isPresent()){
                throw new Exception("Ya existe una postulacion a este nombre");
            }
            else if(prospect.getGradeAverage()<monitoring.getAverageGrade() || prospect.getGradeCourse()< monitoring.getCourseGrade()){
                throw new Exception("No cumple con los requisitos suficientes para aplicar a la vacante");
            }
            else {
                monitoringMonitorRepository.save(new MonitoringMonitor(monitoring, posible.get()));
                return posible.get();
            }
        }

        Monitor monitor = new Monitor(prospect.getCode(), prospect.getName(), prospect.getLastName(), prospect.getSemester(), prospect.getGradeAverage(), prospect
                .getGradeCourse(), prospect.getEmail(), monitoring);

        monitorRepository.save(monitor);
        monitoringMonitorRepository.save(new MonitoringMonitor(monitoring, monitor));
        return monitor;
    }

    @Override
    public Monitor update(Monitor entity) throws Exception {
        return null;
    }

    @Override
    public void delete(Monitor entity) throws Exception {

    }

    @Override
    public void deleteById(String s) throws Exception {

    }

    @Override
    public void validate(Monitor entity) throws Exception {

    }

    @Override
    public Long count() {
        return null;
    }

    public List<Monitor> findPerCourse(String course) throws Exception {

        Optional<Monitoring> monitoring = monitoringRepository.findByCourse(courseRepository.findByName(course));
        if(!monitoring.isPresent()){
            throw new Exception("No existe una monitoria con este nombre");
        }
        List<MonitoringMonitor> list =  monitoringMonitorRepository.findByMonitoring(monitoring.get());
        if(!list.isEmpty()){
            List<Monitor> newList = new ArrayList<>();
            for(MonitoringMonitor mon:list){
                newList.add(mon.getMonitor());
            }
            return newList;
        }
        throw new Exception("No hay monitores o candidatos para este curso");
    }
}
