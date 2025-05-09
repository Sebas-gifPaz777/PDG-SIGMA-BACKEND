package com.pdg.sigma;

import com.pdg.sigma.controller.DepartmentHeadController;
import com.pdg.sigma.dto.DepartmentHeadDTO;
import com.pdg.sigma.repository.HeadProgramRepository; // Import the repository
import com.pdg.sigma.service.DepartmentHeadService; // Use the interface
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DepartmentHeadController.class)
@ComponentScan(basePackages = "com.pdg.sigma.util")
public class HeadDepartmentTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentHeadService departmentHeadService; // Mock the interface

    @MockBean
    private HeadProgramRepository headProgramRepository; // Mock the repository

    @Test
    @WithMockUser(roles = "jfedpto")
    public void testGetDepartmentHeadProfile_Success() throws Exception {
        String departmentHeadId = "12345";
        DepartmentHeadDTO mockDepartmentHead = new DepartmentHeadDTO("Escuela de Ingeniería", "Ingeniería de Sistemas", "Jefe de Departamento", "Carlos Gómez");

        when(departmentHeadService.getProfile(departmentHeadId)).thenReturn(mockDepartmentHead);

        mockMvc.perform(get("/department-head/profile/{id}", departmentHeadId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.school").value("Escuela de Ingeniería"))
                .andExpect(jsonPath("$.program").value("Ingeniería de Sistemas"))
                .andExpect(jsonPath("$.rol").value("Jefe de Departamento"))
                .andExpect(jsonPath("$.name").value("Carlos Gómez"));
    }

    @Test
    @WithMockUser(roles = "jfedpto")
    public void testGetDepartmentHeadProfile_NotFound() throws Exception {
        String departmentHeadId = "99999";

        when(departmentHeadService.getProfile(departmentHeadId)).thenThrow(new Exception("No existe jefe con este id"));

        mockMvc.perform(get("/department-head/profile/{id}", departmentHeadId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe jefe con este id"));
    }
}