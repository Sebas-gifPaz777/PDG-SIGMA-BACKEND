package com.pdg.sigma.service;

import com.pdg.sigma.domain.Activity;
import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.domain.StateActivity;
import com.pdg.sigma.dto.ActivityDTO;
import com.pdg.sigma.repository.ActivityRepository;
import com.pdg.sigma.repository.MonitorRepository;
import com.pdg.sigma.repository.ProfessorRepository;
import com.pdg.sigma.repository.ProspectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivityServiceImpl implements ActivityService{

    @Autowired
    private MonitorRepository monitorRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ProspectRepository prospectRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    public List<ActivityDTO> findAll() {
        return null;
    }

    @Override
    public Optional<ActivityDTO> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public ActivityDTO save(ActivityDTO entity) throws Exception {
        return null;
    }

    @Override
    public ActivityDTO update(ActivityDTO entity) throws Exception {
        return null;
    }

    @Override
    public void delete(ActivityDTO entity) throws Exception {

    }

    @Override
    public void deleteById(Integer integer) throws Exception {

    }

    @Override
    public void validate(ActivityDTO entity) throws Exception {

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
        System.out.println(id);
        Optional<Activity> activity = activityRepository.findById(Integer.parseInt(id));
        System.out.println("Process to get");
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
            System.out.println("Process to save");
            activityRepository.save(activity.get());
            return true;
        }
        else
            throw new Exception("No se encontró una actividad con este id");

    }
}
