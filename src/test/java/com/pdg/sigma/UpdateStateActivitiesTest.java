package com.pdg.sigma;

import com.pdg.sigma.controller.ActivityController;
import com.pdg.sigma.service.ActivityServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing; 
import static org.mockito.Mockito.doThrow; 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ActivityController.class)
@ComponentScan(basePackages = "com.pdg.sigma.util")
public class UpdateStateActivitiesTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityServiceImpl activityService; // Mock para simular el servicio

    // @Test
    // public void testSetActivityState_Success() throws Exception {
    //     String activityId = "1";
    //     Mockito.when(activityService.updateState(activityId)).thenReturn(true); 

    //     mockMvc.perform(put("/activity/updateState") 
    //                     .contentType(MediaType.APPLICATION_JSON) 
    //                     .content(activityId))
    //                     .andExpect(status().isOk())
    //                     .andExpect(content().string("Estado cambiado"));

    //     Mockito.verify(activityService).updateState(activityId);
    // }

    // @Test
    // public void testSetActivityState_NotFound() throws Exception {
    //     String activityId = "999"; // ID inexistente
    //     Mockito.when(activityService.updateState(activityId))
    //            .thenThrow(new Exception("No se encontró una actividad con este id"));

    //     mockMvc.perform(put("/activity/updateState") 
    //                     .contentType(MediaType.APPLICATION_JSON) 
    //                     .content(activityId))
    //                     .andExpect(status().isInternalServerError());
    //             // .andExpect(content().string("No se encontró una actividad con este id"));

    //     Mockito.verify(activityService).updateState(activityId);
    // }
}