package com.pdg.sigma.dto;

import com.pdg.sigma.domain.*;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
public class ActivityDTO {
    private Integer id;
    private String name;
    private Date creaction;
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

    public ActivityDTO(String userId){
        this.userId = userId;
    }
    public ActivityDTO(Activity activity){
        this.id= activity.getId();
        this.name= activity.getName();
        this.creaction = activity.getCreation();
        this.finish = activity.getFinish();
        this.roleCreator = activity.getRoleCreator();
        this.roleResponsable = activity.getRoleResponsable();
        this.category = activity.getCategory();
        this.description = activity.getDescription();
        this.monitoring = activity.getMonitoring();
        this.professor = activity.getProfessor();
        this.monitor = activity.getMonitor();
        this.state = activity.getState();
    }
    public ActivityDTO(String name, Date creaction, Date finish, String category, String description, String course, String creatorName, String responsableName, String state, String type){

    }
}