package com.pdg.sigma.service;

import com.pdg.sigma.domain.Activity;
import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.domain.Monitoring;
import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.domain.StateActivity;
import com.pdg.sigma.dto.ActivityDTO;
import com.pdg.sigma.dto.ActivityRequestDTO;
import com.pdg.sigma.repository.ActivityRepository;
import com.pdg.sigma.repository.MonitorRepository;
import com.pdg.sigma.repository.MonitoringRepository;
import com.pdg.sigma.repository.ProfessorRepository;
import com.pdg.sigma.repository.ProspectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.validation.OverridesAttribute;

@Service
public class ActivityServiceImpl implements ActivityService{

    @Autowired
    private MonitorRepository monitorRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ProspectRepository prospectRepository;

    @Autowired
    private MonitoringRepository monitoringRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    public ActivityDTO update(ActivityRequestDTO updatedActivity) throws Exception {
        Activity activity = activityRepository.findById(updatedActivity.getId())
            .orElseThrow(() -> new Exception("Activity not found"));

        activity.setName(updatedActivity.getName());
        activity.setCreation(updatedActivity.getCreation());
        activity.setFinish(updatedActivity.getFinish());
        activity.setRoleCreator(updatedActivity.getRoleCreator());
        activity.setRoleResponsable(updatedActivity.getRoleResponsable());
        activity.setCategory(updatedActivity.getCategory());
        activity.setDescription(updatedActivity.getDescription());
        if (updatedActivity.getState() != null) {
            activity.setState(StateActivity.valueOf(updatedActivity.getState().toUpperCase()));
        }
        activity.setSemester(updatedActivity.getSemester());
        activity.setDelivey(updatedActivity.getDelivey());

        if (updatedActivity.getMonitoringId() != null) {
            activity.setMonitoring(monitoringRepository.findById(updatedActivity.getMonitoringId().longValue())
                .orElseThrow(() -> new Exception("Monitoring not found")));
        }

        if (updatedActivity.getProfessorId() != null) {
            activity.setProfessor(professorRepository.findById(updatedActivity.getProfessorId().toString())
                .orElseThrow(() -> new Exception("Professor not found")));
        }

        if (updatedActivity.getMonitorId() != null) {
            activity.setMonitor(monitorRepository.findById(updatedActivity.getMonitorId().toString())
                .orElseThrow(() -> new Exception("Monitor not found")));
        }

        Activity updatedEntity = activityRepository.save(activity);

        return new ActivityDTO(updatedEntity);
    }

    @Override
    public Activity update(Activity entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void delete(Activity entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void validate(Activity entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    @Override
    public Optional<Activity> findById(Integer id) {
        return activityRepository.findById(id);
    }


    public ActivityDTO save(ActivityRequestDTO dto) throws Exception {

        Monitoring monitoring = monitoringRepository.findById(dto.getMonitoringId().longValue())
        .orElseThrow(() -> new Exception("Monitoring not found"));

        Professor professor = dto.getProfessorId() != null 
                ? professorRepository.findById(dto.getProfessorId().toString())
                    .orElseThrow(() -> new Exception("Professor not found")) 
                : null;

        Monitor monitor = dto.getMonitorId() != null 
                ? monitorRepository.findById(dto.getMonitorId().toString())
                    .orElseThrow(() -> new Exception("Monitor not found")) 
                : null;

        Activity activity = new Activity(
            dto.getName(),
            new Date(),
            dto.getFinish(),
            dto.getRoleCreator(),
            dto.getRoleResponsable(),
            dto.getCategory(),
            dto.getDescription(),
            monitoring,
            professor,
            monitor,
            StateActivity.PENDIENTE,
            dto.getSemester(),
            dto.getDelivey(),
            new Date()
        );

        Activity savedActivity = save(activity);
        return new ActivityDTO(savedActivity);
    }

    
    @Override
    public Activity save(Activity activity) throws Exception {
        return activityRepository.save(activity);
    }

    @Override
    public void deleteById(Integer id) throws Exception {
        if (!activityRepository.findById(id).isPresent()) {
            throw new Exception("No se encontró la actividad con ID: " + id);
        }
        activityRepository.deleteById(id);
    }


    @Override
    public Long count() {
        return null;
    }

    @Override
    public List<ActivityDTO> findAll(String userId, String role) throws Exception {
        List<Activity> created;
        List<Activity> assigned;
        List<ActivityDTO> list = new ArrayList<>();
        if(!role.equalsIgnoreCase("professor")){ //Se devuelve toda la información junto con quien la creó y a quíen está asignado(solo nombres), permisos sobre este, puede editar o no
            Optional<Monitor>monitor = monitorRepository.findByCode(prospectRepository.findById(userId).get().getCode());
            created = activityRepository.findByMonitorAndRoleCreator(monitor.get(), "M");
            assigned = activityRepository.findByMonitorAndRoleResponsable(monitor.get(), "M");
            if(!created.isEmpty() || !assigned.isEmpty()) {
                if (!created.isEmpty()) {
                    for (Activity activityRaw : created) {
                        ActivityDTO activity = new ActivityDTO(activityRaw);
                        activity.setType("C");
                        if (activity.getRoleResponsable().equals("P"))
                            activity.setResponsableName(activity.getProfessor().getName());
                        else
                            activity.setResponsableName(monitor.get().getName()+" "+monitor.get().getLastName());

                        activity.setCreatorName(monitor.get().getName()+" "+monitor.get().getLastName());
                        list.add(activity);
                    }
                }
                if (!assigned.isEmpty()) {
                    if (!created.isEmpty()) {
                        for (int i = 0; i < created.size(); i++) {
                            for (int y = 0; y < assigned.size(); y++) {
                                if (assigned.get(y).getId().equals(created.get(i).getId()))
                                    assigned.remove(y);
                            }
                        }
                    }
                    for (Activity activityRaw : assigned) {
                        ActivityDTO activity = new ActivityDTO(activityRaw);
                        activity.setType("A");
                        activity.setResponsableName(monitor.get().getName()+" "+monitor.get().getLastName());
                        activity.setCreatorName(activity.getProfessor().getName());
                        list.add(activity);
                    }

                }
                list.sort(Comparator.comparing(ActivityDTO::getState).thenComparing(ActivityDTO::getFinish));

                for(ActivityDTO activityDTO:list){
                    activityDTO.setCourse(activityDTO.getMonitoring().getCourse().getName());
                    activityDTO.setMonitor(null);
                    activityDTO.setMonitoring(null);
                    activityDTO.setProfessor(null);
                }

                return list;
            }
            else
                throw new Exception("No actividades asignadas o creadas");
        }
        else {
            Optional<Professor> professor = professorRepository.findById(userId);
            created = activityRepository.findByProfessorAndRoleCreator(professor.get(), "P");
            assigned = activityRepository.findByProfessorAndRoleResponsable(professor.get(), "P");
            if (!created.isEmpty() || !assigned.isEmpty()) {
                if (!created.isEmpty()) {

                    for (Activity activityRaw : created) {
                        ActivityDTO activity = new ActivityDTO(activityRaw);
                        activity.setType("C");
                        if (activity.getRoleResponsable().equals("M"))
                            activity.setResponsableName(activity.getMonitor().getName()+" "+activity.getMonitor().getLastName());
                        else
                            activity.setResponsableName(professor.get().getName());

                        activity.setCreatorName(professor.get().getName());
                        list.add(activity);
                    }
                }

                if (!assigned.isEmpty()) {
                    if (!created.isEmpty()) {
                        for (int i = 0; i < created.size(); i++) {
                            for (int y = 0; y < assigned.size(); y++) {
                                if (assigned.get(y).getId().equals(created.get(i).getId()))
                                    assigned.remove(y);
                            }
                        }
                    }
                    for (Activity activityRaw : assigned) {
                        ActivityDTO activity = new ActivityDTO(activityRaw);
                        activity.setType("A");
                        activity.setResponsableName(professor.get().getName());
                        activity.setCreatorName(activity.getMonitor().getName()+" "+activity.getMonitor().getLastName());
                        list.add(activity);
                    }

                }

                list.sort(Comparator.comparing(ActivityDTO::getState).thenComparing(ActivityDTO::getFinish));

                for(ActivityDTO activityDTO:list){
                    activityDTO.setCourse(activityDTO.getMonitoring().getCourse().getName());
                    activityDTO.setMonitor(null);
                    activityDTO.setMonitoring(null);
                    activityDTO.setProfessor(null);
                }

                return list;

            } else
                throw new Exception("No actividades asignadas o creadas");
        }
    }

    public boolean updateState(String id) throws Exception {
        Optional<Activity> activity = activityRepository.findById(Integer.parseInt(id));
        if(activity.isPresent()){
            Date delivery = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(delivery);
            calendar.add(Calendar.DAY_OF_MONTH, 2);
            Date extensionDelivery = calendar.getTime();

            if(activity.get().getFinish().after(delivery) || activity.get().getFinish().equals(delivery)
            || delivery.before(extensionDelivery) || delivery.equals(extensionDelivery))
                activity.get().setState(StateActivity.COMPLETADO);
            else
                activity.get().setState(StateActivity.COMPLETADOT);

            activity.get().setDelivey(delivery);
            activityRepository.save(activity.get());
            return true;
        }
        else
            throw new Exception("No se encontró una actividad con este id");

    }

    
}
