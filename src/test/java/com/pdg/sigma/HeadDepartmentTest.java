package com.pdg.sigma;

import com.pdg.sigma.dto.DepartmentHeadDTO;
import com.pdg.sigma.service.DepartmentHeadServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HeadDepartmentTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentHeadServiceImpl departmentHeadService;

    @Test
    public void testGetDepartmentHeadProfile_Success() throws Exception {
        String departmentHeadId = "12345";
        DepartmentHeadDTO mockDepartmentHead = new DepartmentHeadDTO(
                "Escuela de Ingeniería", "Ingeniería de Sistemas", "Jefe de Departamento", "Carlos Gómez");

        Mockito.when(departmentHeadService.getProfile(departmentHeadId)).thenReturn(mockDepartmentHead);

        mockMvc.perform(get("/department-head/profile/{id}", departmentHeadId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.school").value("Escuela de Ingeniería"))
                .andExpect(jsonPath("$.program").value("Ingeniería de Sistemas"))
                .andExpect(jsonPath("$.rol").value("Jefe de Departamento"))
                .andExpect(jsonPath("$.name").value("Carlos Gómez"));
    }

    @Test
    public void testGetDepartmentHeadProfile_NotFound() throws Exception {
        String departmentHeadId = "99999";

        Mockito.when(departmentHeadService.getProfile(departmentHeadId))
                .thenThrow(new Exception("No existe jefe con este id"));

        mockMvc.perform(get("/department-head/profile/{id}", departmentHeadId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe jefe con este id"));
    }
}
