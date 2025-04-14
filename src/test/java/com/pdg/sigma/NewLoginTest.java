package com.pdg.sigma;

import com.pdg.sigma.controller.AuthController;
import com.pdg.sigma.dto.AuthDTO;
import com.pdg.sigma.service.AuthService; 
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; 
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class) 
public class NewLoginTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService; // Mock the interface

    @Test
    void loginUser_Success() throws Exception {
        AuthDTO expectedResponse = new AuthDTO("student");

        // Simula que el servicio devuelve un usuario válido con el rol "student"
        when(authService.loginUser(Mockito.any(AuthDTO.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"123456\",\"password\":\"correctpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("student")); // Verifica que el userId en la respuesta sea "student"
    }

    @Test
    void loginUser_Failure() throws Exception {
        when(authService.loginUser(Mockito.any(AuthDTO.class)))
                .thenThrow(new Exception("No hay un usuario con este id o contraseña"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"000000\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isBadRequest()) // Verifica que el status sea 400
                .andExpect(content().string("No hay un usuario con este id o contraseña")); // Verifica el mensaje de error
    }
}