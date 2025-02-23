package com.pdg.sigma;

import com.pdg.sigma.service.ActivityServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UpdateStateActivitiesTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityServiceImpl activityService;

    @Test
    public void testSetActivityState_Success() throws Exception {
        String activityId = "1";

        Mockito.when(activityService.updateState("1")).thenReturn(true);

        mockMvc.perform(put("http://localhost:5433/activity/updateState")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(activityId))
                .andExpect(status().isOk())
                .andExpect(content().string("Estado cambiado"));
    }

    @Test
    public void testSetActivityState_NotFound() throws Exception {
        String activityId = "999"; // ID inexistente

        Mockito.when(activityService.updateState(activityId))
                .thenThrow(new Exception("No se encontró una actividad con este id"));

        mockMvc.perform(put("http://localhost:5433/activity/updateState")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(activityId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("No se encontró una actividad con este id"));
    }
}
