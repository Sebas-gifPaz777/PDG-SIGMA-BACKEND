package com.pdg.sigma;

import com.pdg.sigma.repository.MonitoringRepository;
import com.pdg.sigma.repository.ProfessorRepository;
import com.pdg.sigma.controller.MonitoringController;
import com.pdg.sigma.service.MonitoringServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MonitoringController.class) 
public class CreateMultipleMonitoringsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MonitoringServiceImpl monitoringService; 

    @MockBean
    private MonitoringRepository monitoringRepository;

    @MockBean
    private ProfessorRepository professorRepository;

    @Test
    public void testProcessListMonitor_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                getValidExcelFileContent()
        );

        when(monitoringService.processListMonitor(any(MultipartFile.class), anyString()))
                .thenReturn("Todas las monitorias han sido creadas");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/monitoring/createAll/{id}", "123") // Use relative URL
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Todas las monitorias han sido creadas"));
    }

    @Test
    public void testProcessListMonitor_EmptyFile() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile("file", "empty.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new byte[0]);

        when(monitoringService.processListMonitor(any(MultipartFile.class), anyString()))
                .thenThrow(new Exception("Incompatibilidad con alguno de los campos del archivo"));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/monitoring/createAll/{id}", "123") // Use relative URL
                        .file(emptyFile))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al procesar el archivo: Incompatibilidad con alguno de los campos del archivo"));
    }

    @Test
    public void testProcessListMonitor_InvalidColumns() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "invalid_columns.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                getInvalidColumnsExcelFileContent()
        );

        when(monitoringService.processListMonitor(any(MultipartFile.class), anyString()))
                .thenThrow(new Exception("Incompatibilidad con alguno de los campos del archivo"));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/monitoring/createAll/{id}", "123") // Use relative URL
                        .file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al procesar el archivo: Incompatibilidad con alguno de los campos del archivo"));
    }

    @Test
    public void testProcessListMonitor_ProfessorNotFound() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "valid.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                getValidExcelFileContent()
        );

        when(monitoringService.processListMonitor(any(MultipartFile.class), anyString()))
                .thenThrow(new Exception("Profesor no encontrado"));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/monitoring/createAll/{id}", "123") // Use relative URL
                        .file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al procesar el archivo: Profesor no encontrado"));
    }

    private byte[] getValidExcelFileContent() {
        // Simulación de contenido de archivo Excel válido en bytes
        return new byte[]{1, 2, 3};
    }

    private byte[] getInvalidColumnsExcelFileContent() {
        // Simulación de contenido de archivo Excel con columnas incorrectas en bytes
        return new byte[]{4, 5, 6};
    }
}