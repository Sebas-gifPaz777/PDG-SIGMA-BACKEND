package com.pdg.sigma;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdg.sigma.dto.ReportDTO;
import com.pdg.sigma.service.MonitoringServiceImpl;
import com.pdg.sigma.controller.MonitoringController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MonitoringController.class) 
@ComponentScan(basePackages = "com.pdg.sigma.util")
class MonitoringControllerReportTest {

    @Autowired
    private MockMvc mockMvc; 

    @MockBean 
    private MonitoringServiceImpl monitoringService;

    @Autowired
    private ObjectMapper objectMapper; 

    private String professorId;
    private Long monitoringId;

    @BeforeEach
    void setUp() {
        professorId = "prof123";
        monitoringId = 1L;
    }

    // @Test
    // void getMonitorsReport_Success() throws Exception {
    //     List<ReportDTO> mockReport = Arrays.asList(
    //             new ReportDTO(5, 2, 1, "Monitor Uno", "Curso A"),
    //             new ReportDTO(8, 0, 0, "Monitor Dos", "Curso B")
    //     );
    //     when(monitoringService.getReportMonitors(professorId)).thenReturn(mockReport);

    //     // Act & Assert: Realiza la petición y verifica la respuesta
    //     mockMvc.perform(get("/monitoring/getMonitorsReport/{idProfessor}", professorId)
    //                     .accept(MediaType.APPLICATION_JSON))
    //             .andExpect(status().isOk()) // Espera 200 OK
    //             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(jsonPath("$.length()").value(mockReport.size())) // Verifica el tamaño del array
    //             .andExpect(jsonPath("$[0].name").value("Monitor Uno"))
    //             .andExpect(jsonPath("$[0].completed").value(5));

    //     // Verify: Verifica que el método del servicio fue llamado
    //     verify(monitoringService, times(1)).getReportMonitors(professorId);
    // }

    @Test
    @WithMockUser(roles = "professor")
    void getMonitorsReport_ProfessorNotFound() throws Exception {
        // Arrange: Configura el mock para lanzar la excepción específica
        String errorMessage = "No hay un profesor con este Id";
        String professorId = "prof123"; // Asegúrate de que professorId esté definido o inicializado
        String role = "professor"; // <-- Define el valor del rol

        when(monitoringService.getReportMonitors(professorId, role)).thenThrow(new Exception(errorMessage));

        // Act & Assert
        mockMvc.perform(get("/monitoring/getMonitorsReport/{idProfessor}/{role}", professorId, role) // <-- Añade {role} y pasa la variable 'role'
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(errorMessage));

        // Verify
        verify(monitoringService, times(1)).getReportMonitors(professorId, role);
        }

    @Test
    @WithMockUser(roles = "professor") 
    void getMonitorsReport_EmptyReport() throws Exception {
        String errorMessage = "No hay reportes por mostrar";
        String role = "professor";
        when(monitoringService.getReportMonitors(professorId,"professor")).thenThrow(new Exception(errorMessage));

        // Act & Assert
        mockMvc.perform(get("/monitoring/getMonitorsReport/{idProfessor}/{role}", professorId, role)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()) 
                .andExpect(content().string(errorMessage)); 

        // Verify
        verify(monitoringService, times(1)).getReportMonitors(professorId,"professor");
    }

    // @Test
    // void getProfessorReport_Success() throws Exception {
    //     List<ReportDTO> mockReport = Collections.singletonList(
    //             new ReportDTO(10, 3, 2, "Profesor X", "Curso C")
    //     );
    //     when(monitoringService.getProfessorReport(professorId)).thenReturn(mockReport);

    //     mockMvc.perform(get("/monitoring/getProfessorReport/{idProfessor}", professorId)
    //                     .accept(MediaType.APPLICATION_JSON))
    //             .andExpect(status().isOk())
    //             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(jsonPath("$.length()").value(1))
    //             .andExpect(jsonPath("$[0].pending").value(10));

    //     verify(monitoringService, times(1)).getProfessorReport(professorId);
    // }

    @Test
    @WithMockUser(roles = "professor") 
    void getProfessorReport_ServiceError() throws Exception {
        String errorMessage = "Error de base de datos";
        when(monitoringService.getProfessorReport(professorId)).thenThrow(new RuntimeException(errorMessage));

        mockMvc.perform(get("/monitoring/getProfessorReport/{idProfessor}", professorId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()) // Espera 500
                .andExpect(content().string(errorMessage)); // Verifica mensaje

        verify(monitoringService, times(1)).getProfessorReport(professorId);
    }

    // --- Tests para getCategoriesReport ---

    @Test
    @WithMockUser
    void getCategoriesReport_Success_WithMonitoringId() throws Exception {
        // Arrange
        Map<String, Object> mockReport = new LinkedHashMap<>();
        mockReport.put("detalle_por_curso", Collections.emptyList()); // Simular datos
        mockReport.put("totales_por_categoria", Arrays.asList(Map.of("categoria", "Test", "cantidad_total", 5L)));
        when(monitoringService.getCategoryReport(eq(professorId), eq(Optional.of(monitoringId))))
                .thenReturn(mockReport);

        // Act & Assert
        mockMvc.perform(get("/monitoring/getCategoriesReport/{professorId}", professorId)
                        .param("monitoringId", String.valueOf(monitoringId)) // Añadir RequestParam
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totales_por_categoria[0].categoria").value("Test"))
                .andExpect(jsonPath("$.totales_por_categoria[0].cantidad_total").value(5));

        // Verify
        verify(monitoringService, times(1)).getCategoryReport(eq(professorId), eq(Optional.of(monitoringId)));
    }

    @Test
    @WithMockUser
    void getCategoriesReport_Success_WithoutMonitoringId() throws Exception {
        // Arrange
        Map<String, Object> mockReport = new LinkedHashMap<>();
        mockReport.put("detalle_por_curso", Collections.emptyList());
        mockReport.put("totales_por_categoria", Arrays.asList(Map.of("categoria", "General", "cantidad_total", 10L)));
        when(monitoringService.getCategoryReport(eq(professorId), eq(Optional.empty())))
                .thenReturn(mockReport);

        // Act & Assert
        mockMvc.perform(get("/monitoring/getCategoriesReport/{professorId}", professorId)
                        // No .param() aquí
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totales_por_categoria[0].categoria").value("General"))
                .andExpect(jsonPath("$.totales_por_categoria[0].cantidad_total").value(10));

        // Verify
        verify(monitoringService, times(1)).getCategoryReport(eq(professorId), eq(Optional.empty()));
    }

    @Test
    @WithMockUser
    void getCategoriesReport_Empty() throws Exception {
        Map<String, Object> mockReport = new LinkedHashMap<>();
        mockReport.put("detalle_por_curso", Collections.emptyList());
        mockReport.put("totales_por_categoria", Collections.emptyList());
        when(monitoringService.getCategoryReport(eq(professorId), eq(Optional.empty())))
                .thenReturn(mockReport);

        // Act & Assert
        mockMvc.perform(get("/monitoring/getCategoriesReport/{professorId}", professorId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // El controller devuelve 200 OK con el objeto vacío
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.detalle_por_curso").isEmpty())
                .andExpect(jsonPath("$.totales_por_categoria").isEmpty());

        // Verify
        verify(monitoringService, times(1)).getCategoryReport(eq(professorId), eq(Optional.empty()));
    }


    @Test
    @WithMockUser
    void getCategoriesReport_ProfessorNotFound() throws Exception {
        // Arrange
        String errorMessage = "Profesor con ID " + professorId + " no encontrado.";
        when(monitoringService.getCategoryReport(eq(professorId), any(Optional.class)))
                .thenThrow(new Exception(errorMessage));

        // Act & Assert
        mockMvc.perform(get("/monitoring/getCategoriesReport/{professorId}", professorId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // Espera 404
                .andExpect(jsonPath("$.error").value(errorMessage)); // Verifica JSON de error

        // Verify
        verify(monitoringService, times(1)).getCategoryReport(eq(professorId), any(Optional.class));
    }

    @Test
    @WithMockUser
    void getCategoriesReport_InternalError() throws Exception {
        // Arrange
        String errorMessage = "Error interno al generar el reporte de categorías."; 
        when(monitoringService.getCategoryReport(eq(professorId), any(Optional.class)))
                .thenThrow(new RuntimeException("DB connection failed")); 

        // Act & Assert
        mockMvc.perform(get("/monitoring/getCategoriesReport/{professorId}", professorId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()) // Espera 500
                .andExpect(jsonPath("$.error").value(errorMessage)); // Verifica JSON de error genérico

        // Verify
        verify(monitoringService, times(1)).getCategoryReport(eq(professorId), any(Optional.class));
    }

    @Test
    @WithMockUser
    void getProfessorMonthlyAttendance_Success_WithoutMonitoringId() throws Exception {
        // Arrange
        List<Map<String, Object>> mockReport = Arrays.asList(
                Map.of("mes", "Marzo", "semestre", "2024-1", "total_mes", 15L, "asistencia_por_curso", Collections.emptyList()),
                Map.of("mes", "Abril", "semestre", "2024-1", "total_mes", 20L, "asistencia_por_curso", Collections.emptyList())
        );
        when(monitoringService.getMonthlyAttendanceReport(eq(professorId), eq(Optional.empty())))
                .thenReturn(mockReport);

        // Act & Assert
        mockMvc.perform(get("/monitoring/getAttendanceReport/{professorId}", professorId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].mes").value("Marzo"))
                .andExpect(jsonPath("$[1].total_mes").value(20));

        // Verify
        verify(monitoringService, times(1)).getMonthlyAttendanceReport(eq(professorId), eq(Optional.empty()));
    }
    
    @Test
    @WithMockUser
    void getProfessorMonthlyAttendance_Success_WithMonitoringId() throws Exception {
        // Arrange
        List<Map<String, Object>> mockReport = Collections.singletonList(
             Map.of("mes", "Abril", "semestre", "2024-1", "total_mes", 8L, "asistencia_por_curso", Collections.emptyList())
        );
        when(monitoringService.getMonthlyAttendanceReport(eq(professorId), eq(Optional.of(monitoringId))))
                .thenReturn(mockReport);

        // Act & Assert
        mockMvc.perform(get("/monitoring/getAttendanceReport/{professorId}", professorId)
                        .param("monitoringId", String.valueOf(monitoringId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].total_mes").value(8));

        // Verify
        verify(monitoringService, times(1)).getMonthlyAttendanceReport(eq(professorId), eq(Optional.of(monitoringId)));
    }


    @Test
    @WithMockUser
    void getProfessorMonthlyAttendance_Empty() throws Exception {
        // Arrange: Servicio devuelve lista vacía
        List<Map<String, Object>> mockReport = Collections.emptyList();
        when(monitoringService.getMonthlyAttendanceReport(eq(professorId), eq(Optional.empty())))
                .thenReturn(mockReport);

        // Act & Assert
        mockMvc.perform(get("/monitoring/getAttendanceReport/{professorId}", professorId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Espera 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verifica la estructura específica de mensaje + data vacía
                .andExpect(jsonPath("$.message").value("No se encontraron datos de asistencia para los criterios seleccionados."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());

        // Verify
        verify(monitoringService, times(1)).getMonthlyAttendanceReport(eq(professorId), eq(Optional.empty()));
    }

    // @Test
    // void getProfessorMonthlyAttendance_NotFound() throws Exception {
    //     // Arrange
    //     String errorMessage = "Monitoría con ID " + monitoringId + " no encontrada.";
    //     when(monitoringService.getMonthlyAttendanceReport(eq(professorId), eq(Optional.of(monitoringId))))
    //             .thenThrow(new Exception(errorMessage));

    //     // Act & Assert
    //     mockMvc.perform(get("/monitoring/getAttendanceReport/{professorId}", professorId)
    //                      .param("monitoringId", String.valueOf(monitoringId))
    //                     .accept(MediaType.APPLICATION_JSON))
    //             .andExpect(status().isNotFound()) // Espera 404
    //             .andExpect(jsonPath("$.error").value(errorMessage)); // Verifica JSON de error

    //     // Verify
    //     verify(monitoringService, times(1)).getMonthlyAttendanceReport(eq(professorId), eq(Optional.of(monitoringId)));
    // }

    @Test
    @WithMockUser
    void getProfessorMonthlyAttendance_InternalError() throws Exception {
        // Arrange
        String expectedErrorMessage = "Error al generar el reporte de asistencia: DB Error"; // Mensaje del controller + causa
        when(monitoringService.getMonthlyAttendanceReport(eq(professorId), eq(Optional.empty())))
                .thenThrow(new RuntimeException("DB Error")); // Simular error inesperado

        // Act & Assert
        mockMvc.perform(get("/monitoring/getAttendanceReport/{professorId}", professorId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()) // Espera 500
                .andExpect(jsonPath("$.error").value(expectedErrorMessage)); // Verifica JSON de error genérico

        // Verify
        verify(monitoringService, times(1)).getMonthlyAttendanceReport(eq(professorId), eq(Optional.empty()));
    }

}