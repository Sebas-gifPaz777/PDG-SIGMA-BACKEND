package com.pdg.sigma;

import com.pdg.sigma.controller.ActivityController;
import com.pdg.sigma.domain.Activity;
import com.pdg.sigma.dto.ActivityDTO;
import com.pdg.sigma.repository.ActivityRepository;
import com.pdg.sigma.repository.CourseProfessorRepository;
import com.pdg.sigma.service.ActivityService;
import com.pdg.sigma.service.CourseServiceImpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class GetActivtiesListTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

    @MockBean
    private CourseServiceImpl courseService;

    @MockBean
    private CourseProfessorRepository courseProfessorRepository;

    @MockBean
    private ActivityRepository activityRepository;

    @Test
    @WithMockUser
    public void testGetActivitiesPerUser_Monitor_Success() throws Exception {
        String userId = "123";
        String role = "monitor";
        ActivityDTO a = new ActivityDTO(new Activity());
        ActivityDTO b = new ActivityDTO(new Activity());

        a.setId(1);
        a.setName("Actividad 1");

        b.setId(2);
        b.setName("Actividad 2");
        List<ActivityDTO> mockActivities = List.of(a, b);

        Mockito.when(activityService.findAll(userId, role)).thenReturn(mockActivities);

        mockMvc.perform(get("/activity/findAll/{userId}/{role}", userId, role)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Actividad 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Actividad 2"));
    }

    @Test
    @WithMockUser(roles = "professor")
    public void testGetActivitiesPerUser_Professor_Success() throws Exception {
        String userId = "456";
        String role = "professor";
        ActivityDTO a = new ActivityDTO(new Activity());
        a.setId(3);
        a.setName("Clase 1");
        List<ActivityDTO> mockActivities = List.of(a);

        Mockito.when(activityService.findAll(userId, role)).thenReturn(mockActivities);

        mockMvc.perform(get("/activity/findAll/{userId}/{role}", userId, role)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("Clase 1"));
    }

    @Test
    @WithMockUser
    public void testGetActivitiesPerUser_NotFound() throws Exception {
        String userId = "999"; // Usuario sin actividades
        String role = "monitor";

        Mockito.when(activityService.findAll(userId, role))
                .thenThrow(new Exception("No actividades asignadas o creadas"));

        mockMvc.perform(get("/activity/findAll/{userId}/{role}", userId, role)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No actividades asignadas o creadas"));
    }
}