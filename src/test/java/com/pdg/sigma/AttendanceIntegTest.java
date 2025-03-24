// package com.pdg.sigma;

// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import static org.mockito.ArgumentMatchers.any;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import static org.mockito.Mockito.when;
// import org.mockito.MockitoAnnotations;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.dao.DataIntegrityViolationException;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.pdg.sigma.controller.AttendanceController;
// import com.pdg.sigma.domain.Attendance;
// import com.pdg.sigma.domain.Student;
// import com.pdg.sigma.dto.ActivityRequestDTO;
// import com.pdg.sigma.service.AttendanceServiceImpl;

// @WebMvcTest(AttendanceController.class)
// public class AttendanceIntegTest {

//     private MockMvc mockMvc;

//     @Mock
//     private AttendanceServiceImpl attendanceService;

//     @InjectMocks
//     private AttendanceController attendanceController;

//     private ObjectMapper objectMapper = new ObjectMapper();

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//         mockMvc = MockMvcBuilders.standaloneSetup(attendanceController).build();
//     }

//     @Test
//     void testCreateAttendance_Success() throws Exception {
//         Attendance attendance = new Attendance();
//         ActivityRequestDTO requestDTO = new ActivityRequestDTO();
//         requestDTO.setName("TEST Actividad 1");
//         requestDTO.setDescription("Descripción de prueba");
//         requestDTO.setState("PENDIENTE");
//         attendance.setStudent(new Student("12345", "Juanito Perez"));

//         when(attendanceService.save(any(Attendance.class))).thenReturn(attendance);

//         mockMvc.perform(post("/attendance/create")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(attendance)))
//                 .andExpect(status().isCreated());
//     }

//     @Test
//     void testCreateAttendance_DuplicateEntry() throws Exception {
//         Attendance attendance = new Attendance();
//         ActivityRequestDTO requestDTO = new ActivityRequestDTO();
//         requestDTO.setName("TEST Actividad 1");
//         requestDTO.setDescription("Descripción de prueba");
//         requestDTO.setState("PENDIENTE");
//         attendance.setStudent(new Student("12345", "Juanito Perez"));

//         attendance.setStudent(new Student("12345", "John Doe"));

//         when(attendanceService.save(any(Attendance.class)))
//                 .thenThrow(new DataIntegrityViolationException("Duplicate entry"));

//         mockMvc.perform(post("/attendance/create")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(attendance)))
//                 .andExpect(status().isConflict())
//                 .andExpect(content().string("Error: La asistencia ya existe."));
//     }

//     @Test
//     void testCheckAttendanceExists_True() throws Exception {
//         when(attendanceService.findByActivityAndStudent(1, "12345"))
//                 .thenReturn(Optional.of(new Attendance()));

//         mockMvc.perform(get("/attendance/check/1/12345"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("true"));
//     }

//     @Test
//     void testCheckAttendanceExists_False() throws Exception {
//         when(attendanceService.findByActivityAndStudent(1, "12345"))
//                 .thenReturn(Optional.empty());

//         mockMvc.perform(get("/attendance/check/1/12345"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("false"));
//     }
// }
