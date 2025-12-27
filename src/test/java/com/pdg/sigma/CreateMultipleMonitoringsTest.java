package com.pdg.sigma;

import com.pdg.sigma.domain.*;
import com.pdg.sigma.repository.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateMultipleMonitoringsTest {

    @Autowired
    private MockMvc mockMvc;

    // NO mockeamos el servicio, queremos probar su lógica real.
    // @MockBean
    // private MonitoringServiceImpl monitoringService;

    // SI mockeamos los repositorios para controlar la capa de datos.
    @MockBean private MonitoringRepository monitoringRepository;
    @MockBean private ProfessorRepository professorRepository;
    @MockBean private SchoolRepository schoolRepository;
    @MockBean private ProgramRepository programRepository;
    @MockBean private CourseRepository courseRepository;

    private Professor mockProfessor;
    private School mockSchool;
    private Program mockProgram;
    private Course mockCourse;

    @BeforeEach
    void setUp() {
        // Pre-configurar
        mockProfessor = new Professor();
        mockProfessor.setId("prof123");

        mockSchool = new School();
        mockSchool.setName("Ingeniería");

        mockProgram = new Program();
        mockProgram.setName("Sistemas");

        mockCourse = new Course();
        mockCourse.setName("Cálculo");
    }

    private byte[] createExcelFile(String[] headers, Object[][] data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Monitorias");

            // Header
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Data
            for (int i = 0; i < data.length; i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < data[i].length; j++) {
                    Cell cell = row.createCell(j);
                    if (data[i][j] instanceof String) {
                        cell.setCellValue((String) data[i][j]);
                    } else if (data[i][j] instanceof Number) {
                        cell.setCellValue(((Number) data[i][j]).doubleValue());
                    }
                }
            }
            workbook.write(out);
            return out.toByteArray();
        }
    }

    @Test
    @WithMockUser // Usuario autenticado para pasar la seguridad de Spring
    public void createMultipleMonitoring_Success() throws Exception {
        // Crear un archivo Excel válido en memoria
        String[] headers = {"FACULTAD", "PROGRAMA", "CURSO", "FECHA INICIO", "FECHA FINALIZACION", "PERIODO", "PROMEDIO ACUMULADO", "PROMEDIO MATERIA"};
        Object[][] data = {{"Ingeniería", "Sistemas", "Cálculo", "01-08-2024", "01-12-2024", "2024-2", 4.5, 4.5}};
        byte[] excelContent = createExcelFile(headers, data);

        MockMultipartFile file = new MockMultipartFile(
                "file", "monitorias.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", excelContent
        );

        when(professorRepository.findById("prof123")).thenReturn(Optional.of(mockProfessor));
        when(schoolRepository.findByName("Ingeniería")).thenReturn(Optional.of(mockSchool));
        when(programRepository.findByName("Sistemas")).thenReturn(Optional.of(mockProgram));
        when(courseRepository.findByName("Cálculo")).thenReturn(Optional.of(mockCourse));
        when(monitoringRepository.findByCourse(any(Course.class))).thenReturn(Optional.empty()); // Clave: la monitoria no existe aún
        when(monitoringRepository.save(any(Monitoring.class))).thenReturn(new Monitoring());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart("/monitoring/createAll/{id}", "prof123")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Todas las monitorias han sido creadas"));
    }

    @Test
    @WithMockUser
    public void createMultipleMonitoring_FailsWhenMonitoringAlreadyExists() throws Exception {
        // Arrange
        String[] headers = {"FACULTAD", "PROGRAMA", "CURSO", "FECHA INICIO", "FECHA FINALIZACION", "PERIODO", "PROMEDIO ACUMULADO", "PROMEDIO MATERIA"};
        Object[][] data = {{"Ingeniería", "Sistemas", "Cálculo", "01-08-2024", "01-12-2024", "2024-2", 4.5, 4.5}};
        byte[] excelContent = createExcelFile(headers, data);

        MockMultipartFile file = new MockMultipartFile(
                "file", "monitorias.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", excelContent
        );

        // Configurar mocks, pero esta vez la monitoria YA existe
        when(professorRepository.findById("prof123")).thenReturn(Optional.of(mockProfessor));
        when(schoolRepository.findByName("Ingeniería")).thenReturn(Optional.of(mockSchool));
        when(programRepository.findByName("Sistemas")).thenReturn(Optional.of(mockProgram));
        when(courseRepository.findByName("Cálculo")).thenReturn(Optional.of(mockCourse));
        when(monitoringRepository.findByCourse(any(Course.class))).thenReturn(Optional.of(new Monitoring())); // Clave: la monitoria YA existe

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart("/monitoring/createAll/{id}", "prof123")
                        .file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al procesar el archivo: Al menos una monitoria está creada"));
    }

    @Test
    @WithMockUser
    public void createMultipleMonitoring_FailsWithInvalidHeader() throws Exception {
        
        // Archivo con una cabecera incorrecta
        String[] headers = {"FACULTAD INCORRECTA", "PROGRAMA", "CURSO", "FECHA INICIO", "FECHA FINALIZACION", "PERIODO", "PROMEDIO ACUMULADO", "PROMEDIO MATERIA"};
        Object[][] data = {{"Ingeniería", "Sistemas", "Cálculo", "01-08-2024", "01-12-2024", "2024-2", 4.5, 4.5}};
        byte[] excelContent = createExcelFile(headers, data);

        MockMultipartFile file = new MockMultipartFile(
                "file", "invalid.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", excelContent
        );

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart("/monitoring/createAll/{id}", "prof123")
                        .file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al procesar el archivo: Incompatibilidad con alguno de los campos del archivo"));
    }
    
    @Test
    @WithMockUser
    public void createMultipleMonitoring_FailsWithEmptyDataCell() throws Exception {
        // Arrange
        // Archivo con un campo de dato vacío
        String[] headers = {"FACULTAD", "PROGRAMA", "CURSO", "FECHA INICIO", "FECHA FINALIZACION", "PERIODO", "PROMEDIO ACUMULADO", "PROMEDIO MATERIA"};
        Object[][] data = {{"Ingeniería", "", "Cálculo", "01-08-2024", "01-12-2024", "2024-2", 4.5, 4.5}}; // Programa vacío
        byte[] excelContent = createExcelFile(headers, data);

        MockMultipartFile file = new MockMultipartFile(
                "file", "invalid_data.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", excelContent
        );

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart("/monitoring/createAll/{id}", "prof123")
                        .file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al procesar el archivo: Incompatibilidad con alguno de los campos del archivo"));
    }
}