package com.pdg.sigma.dto;

import java.util.Date;

import com.pdg.sigma.domain.Activity;
import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.domain.Monitoring;
import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.domain.StateActivity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Setter
public class ActivityDTO {
    private Integer id;
    private String name;
    private Date creation;
    private Date finish;
    private String roleCreator;
    private String roleResponsable;
    private String category;
    private String description;
    private Monitoring monitoring;
    private Professor professor;
    private Monitor monitor;
    private StateActivity state;
    private String type;
    private String responsableName;
    private String creatorName;
    private String course;
    private String userId;
    private Date edited;
    private Date delivey;
    private String semester;

    public ActivityDTO(String userId){
        this.userId = userId;
    }
    public ActivityDTO(Activity activity){
        this.id= activity.getId();
        this.name= activity.getName();
        this.creation = activity.getCreation();
        this.finish = activity.getFinish();
        this.roleCreator = activity.getRoleCreator();
        this.roleResponsable = activity.getRoleResponsable();
        this.category = activity.getCategory();
        this.description = activity.getDescription();
        this.monitoring = activity.getMonitoring();
        this.professor = activity.getProfessor();
        this.monitor = activity.getMonitor();
        this.state = activity.getState();
        this.edited = activity.getEdited();
        this.delivey = activity.getDelivey();
        this.semester = activity.getSemester();
    }
    public ActivityDTO(String name, Date creation, Date finish, String category, String description, String course, String creatorName, String responsableName, String state, String type){

    }
}