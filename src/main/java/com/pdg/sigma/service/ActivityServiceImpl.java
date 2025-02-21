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
        List<Activity> list = activityRepository.findAll();
        List<ActivityDTO> newList = new ArrayList<>();

        for (Activity activity : list) {
            newList.add(new ActivityDTO(activity
            ));
        }

        return newList;

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
    public List<ActivityDTO> findAll(String userId) throws Exception {
        Optional<Monitor>monitor = monitorRepository.findByCode(prospectRepository.findById(userId).get().getCode());
        List<Activity> created;
        List<Activity> assigned;
        List<ActivityDTO> list = new ArrayList<>();
        if(monitor.isPresent()){ //Se devuelve toda la información junto con quien la creó y a quíen está asignado(solo nombres), permisos sobre este, puede editar o no
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
                            activity.setResponsableName(activity.getMonitor().getName());

                        activity.setCreatorName(monitor.get().getName());
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
                        activity.setResponsableName(monitor.get().getName());
                        activity.setCreatorName(activity.getProfessor().getName());
                        list.add(activity);
                    }

                }
                Map<StateActivity, Integer> priorityMap = Map.of(
                        StateActivity.PENDIENTE, 1,
                        StateActivity.COMPLETADO, 2,
                        StateActivity.COMPLETADOT, 3

                );
                list.sort(Comparator.comparing(ActivityDTO::getState).thenComparing(ActivityDTO::getFinish));

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
                            activity.setResponsableName(activity.getMonitor().getName());
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
                        activity.setCreatorName(activity.getMonitor().getName());
                        list.add(activity);
                    }

                }

                list.sort(Comparator.comparing(ActivityDTO::getState).thenComparing(ActivityDTO::getFinish));

                return list;

            } else
                throw new Exception("No actividades asignadas o creadas");
        }
    }
}