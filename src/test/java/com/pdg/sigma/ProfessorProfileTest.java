package com.pdg.sigma;

import com.pdg.sigma.controller.ProfessorController;
import com.pdg.sigma.dto.ProfessorDTO;
import com.pdg.sigma.service.ProfessorServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProfessorController.class)
@AutoConfigureMockMvc
@ComponentScan(basePackages = "com.pdg.sigma.util")
public class ProfessorProfileTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfessorServiceImpl professorService;

    // Happy Path**: El profesor existe y se devuelve su perfil correctamente.
    @Test
    @WithMockUser(roles="professor")
    public void testGetProfessorProfile_Success() throws Exception {
        String professorId = "12345";
        ProfessorDTO mockProfessor = new ProfessorDTO("Barberi de Ingeniería, Diseño y Ciencias Aplicadas", "Ingeniaría de Sistemas", "Profesor", "Juan Pérez");

        // Simulación de servicio
        Mockito.when(professorService.getProfile(professorId)).thenReturn(mockProfessor);

        mockMvc.perform(get("http://localhost:5433/professor/profile/{id}", professorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 200
                .andExpect(jsonPath("$.school").value("Barberi de Ingeniería, Diseño y Ciencias Aplicadas"))
                .andExpect(jsonPath("$.program").value("Ingeniaría de Sistemas"))
                .andExpect(jsonPath("$.rol").value("Profesor"))
                .andExpect(jsonPath("$.name").value("Juan Pérez"));
    }

    // Not Happy Path**: El profesor no existe y se lanza una excepción.
    @Test
    @WithMockUser(roles="professor")
    public void testGetProfessorProfile_NotFound() throws Exception {
        String invalidId = "99999";

        // Simulación de excepción
        Mockito.when(professorService.getProfile(invalidId))
                .thenThrow(new Exception("No existe profesor con este ID"));

        mockMvc.perform(get("http://localhost:5433/professor/profile/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // HTTP 404
                .andExpect(content().string("No existe profesor con este ID"));
    }

    // Not Happy Path**: El profesor no tiene cursos asignados.
    @Test
    @WithMockUser(roles="professor")
    public void testGetProfessorProfile_NoCoursesAssigned() throws Exception {
        String professorId = "55555";

        // Simulación de excepción
        Mockito.when(professorService.getProfile(professorId))
                .thenThrow(new Exception("No tiene asignado cursos para este semestre"));

        mockMvc.perform(get("http://localhost:5433/professor/profile/{id}", professorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // HTTP 404
                .andExpect(content().string("No tiene asignado cursos para este semestre"));
    }
}
