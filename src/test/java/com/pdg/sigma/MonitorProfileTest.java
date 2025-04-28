package com.pdg.sigma;

import com.pdg.sigma.dto.MonitorDTO;
import com.pdg.sigma.controller.MonitorController; 
import com.pdg.sigma.service.MonitorServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MonitorController.class)
public class MonitorProfileTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MonitorServiceImpl monitorService;

    @Test
    void testProfileMonitorFound() throws Exception {
        String monitorId = "123";
        MonitorDTO mockMonitor = new MonitorDTO("Engineering", "Software", "Monitor", "John Doe");

        Mockito.when(monitorService.getProfile(monitorId)).thenReturn(mockMonitor);

        mockMvc.perform(get("/monitor/profile/{id}", monitorId) // Usa la ruta relativa del controlador
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.school").value("Engineering"))
                .andExpect(jsonPath("$.program").value("Software"))
                .andExpect(jsonPath("$.rol").value("Monitor"))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testProfileMonitorNotFound() throws Exception {
        String monitorId = "999";

        Mockito.when(monitorService.getProfile(monitorId)).thenThrow(new Exception("No existe monitor con este ID"));

        mockMvc.perform(get("/monitor/profile/{id}", monitorId) // Usa la ruta relativa del controlador
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe monitor con este ID"));
    }
}