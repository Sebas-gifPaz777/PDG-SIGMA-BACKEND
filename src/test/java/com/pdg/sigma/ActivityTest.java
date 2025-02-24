package com.pdg.sigma;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdg.sigma.controller.ActivityController;
import com.pdg.sigma.dto.ActivityDTO;
import com.pdg.sigma.dto.ActivityRequestDTO;
import com.pdg.sigma.domain.StateActivity;
import com.pdg.sigma.service.ActivityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ActivityController.class)
public class ActivityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityServiceImpl activityService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        Mockito.reset(activityService);
    }

    // Prueba para "CREATE Activity"
    @Test
    public void testCreateActivity_Success() throws Exception {
        ActivityRequestDTO requestDTO = new ActivityRequestDTO();
        requestDTO.setName("TEST Actividad 2");
        requestDTO.setDescription("Descripción de prueba");
        requestDTO.setState("PENDIENTE");

        ActivityDTO responseDTO = new ActivityDTO();
        responseDTO.setName("TEST Actividad 2");
        responseDTO.setDescription("Descripción de prueba");
        responseDTO.setState(StateActivity.PENDIENTE); 

        when(activityService.save(any(ActivityRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/activity/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.state").value("PENDIENTE"))
                .andExpect(jsonPath("$.name").value("TEST Actividad 2"));
    }

    // Prueba para "UPDATE Activity"
    @Test
    public void testUpdateActivity_Success() throws Exception {
        ActivityRequestDTO requestDTO = new ActivityRequestDTO();
        requestDTO.setId(36);
        requestDTO.setName("Actividad Actualizada");
        requestDTO.setDescription("Nueva descripción");

        ActivityDTO responseDTO = new ActivityDTO();
        responseDTO.setName("Actividad Actualizada");
        responseDTO.setDescription("Nueva descripción");

        when(activityService.update(any(ActivityRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/activity/update") 
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Nueva descripción"))
                .andExpect(jsonPath("$.name").value("Actividad Actualizada"));
    }

    // ueba para "DELETE Activity"
    @Test
    public void testDeleteActivity_Success() throws Exception {
        doNothing().when(activityService).deleteById(anyInt());

        mockMvc.perform(delete("/activity/{id}", 37) 
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(activityService, times(1)).deleteById(37);
    }
}
