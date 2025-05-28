package com.pdg.sigma.service;

import com.pdg.sigma.domain.*;
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

    public List<MonitorDTO> findAllNew() { //Use Monitor selection process
        List<Monitor> baseMonitors = monitorRepository.findAll();
        List<MonitorDTO> list = new ArrayList<>();
        for(Monitor monitor:baseMonitors){
            List<MonitoringMonitor> link = monitoringMonitorRepository.findByMonitor(monitor);
            for(MonitoringMonitor connection:link){
                MonitorDTO monitorDTO = new MonitorDTO(monitor);
                monitorDTO.setCourse(connection.getMonitoring().getCourse().getName());
                list.add(monitorDTO);
            }

        }
        return list;
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
        if(prospect.getGradeAverage()<monitoring.getAverageGrade() || prospect.getGradeCourse()< monitoring.getCourseGrade()){
            throw new Exception("No cumple con los requisitos suficientes para aplicar a la vacante");
        }

        Monitor monitor = new Monitor(prospect.getCode(), prospect.getName(), prospect.getLastName(), prospect.getSemester(), prospect.getGradeAverage(), prospect
                .getGradeCourse(), prospect.getEmail(), monitoring, prospect.getId());

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
    public void deleteById(String id) throws Exception {
        if (id == null || id.isEmpty()) {
            throw new Exception("ID cannot be null or empty");
        }

        try {
            monitorRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("Candidature not found or error while deleting", e);
        }
    }

    @Override
    public void validate(Monitor entity) throws Exception {

    }

    @Override
    public Long count() {
        return null;
    }
    //Get Monitor profile
    //Change the method of create monitor taking into account the columm added "id"
    public MonitorDTO getProfile(String id) throws Exception{
        Optional<Monitor> monitor = monitorRepository.findByIdMonitor(id);
        String school="";
        String program="";
        String role="Monitor";
        if(monitor.isPresent()){
            List<MonitoringMonitor> list = monitoringMonitorRepository.findByMonitor(monitor.get());


            List<String> schools = new ArrayList<>();
            List<String> programs = new ArrayList<>();
            for(int i=0; i<list.size();i++){
                if(!schools.contains(list.get(i).getMonitoring().getSchool().getName())){
                    if(i!= list.size()-1){
                        school = school+list.get(i).getMonitoring().getSchool().getName()+" | ";
                    }
                    else{
                        school = school+list.get(i).getMonitoring().getSchool().getName();
                    }
                    schools.add(list.get(i).getMonitoring().getSchool().getName());
                }
                if(!programs.contains(list.get(i).getMonitoring().getProgram().getName())){
                    if(i!= list.size()-1){
                        program = program+list.get(i).getMonitoring().getProgram().getName()+" | ";
                    }
                    else{
                        program = program+list.get(i).getMonitoring().getProgram().getName();
                    }
                    programs.add(list.get(i).getMonitoring().getProgram().getName());
                }

            }

            MonitorDTO data = new MonitorDTO(school,program,role, monitor.get().getName()+" "+monitor.get().getLastName());

            return data;
        }
        else
            throw new Exception("No existe monitor con este ID");

    }

    //Not used
    /*public List<Monitor> findPerCourse(String course) throws Exception {

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
    }*/
}
