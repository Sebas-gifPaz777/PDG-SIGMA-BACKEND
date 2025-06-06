package com.pdg.sigma.controller;

import com.pdg.sigma.domain.Monitoring;
import com.pdg.sigma.dto.MonitoringDTO;
import com.pdg.sigma.service.MonitoringServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
@RequestMapping("/monitoring")
@RestController
public class MonitoringController {

    @Autowired
    MonitoringServiceImpl monitoringService;

    @RequestMapping(value= "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createMonitoring(@RequestBody MonitoringDTO newMonitoring){
        try{
            Monitoring monitoring = monitoringService.save(newMonitoring);
            if(monitoringService.findById(monitoring.getId()).isPresent())
                return ResponseEntity.status(200).body("Se ha creado una monitoria");

            return ResponseEntity.status(400).body("No se pudo crear la monitoria");
        }catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }

    @RequestMapping(value= "/getA", method = RequestMethod.GET)
    public ResponseEntity<?> getAllMonitoring(){
        try{
            List<Monitoring> listMonitoring = monitoringService.findAll();
            if(!listMonitoring.isEmpty()){
                return ResponseEntity.status(200).body(listMonitoring);
            }

            return ResponseEntity.status(400).body("No hay monitorias en la lista");
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @RequestMapping(value= "/getAllByProfessor/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllMonitoringByProfessor(@PathVariable String id){
        System.out.println(id);
        try{
            List<Monitoring> listMonitoring = monitoringService.findAllByProfessor(id);
            if(!listMonitoring.isEmpty()){
                return ResponseEntity.status(200).body(listMonitoring);
            }

            return ResponseEntity.status(400).body("No hay monitorias en la lista");
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping(value= "/getAllActiveByUserId/{userId}/{role}")
    public ResponseEntity<?> getMonitoringsWithAssignedMonitorsByUserIdAndRole(@PathVariable String userId, @PathVariable String role) {
        System.out.println("getMonitoringsWithAssignedMonitorsByUserIdAndRole called with userId: " + userId + ", role: " + role);

        if (userId == null || userId.trim().isEmpty() || role == null || role.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID de usuario y el rol son obligatorios.");
        }

        String lowerCaseRole = role.toLowerCase();
        if (!"professor".equals(lowerCaseRole) && !"monitor".equals(lowerCaseRole)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rol no válido. Debe ser 'professor' o 'monitor'.");
        }

        try {
            List<Monitoring> monitorings;
            if ("professor".equals(lowerCaseRole)) {
                monitorings = monitoringService.findMonitoringsByProfessorWithAssignedMonitors(userId);
            } else { // "monitor"
                
                monitorings = monitoringService.findMonitoringsByAssignedMonitor(userId); 
            }

            if (monitorings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron monitorías para este usuario y rol.");
            }
            return ResponseEntity.ok(monitorings);

        } catch (IllegalArgumentException e) {
            System.err.println("Argumento inválido en getMonitoringsWithAssignedMonitorsByUserIdAndRole: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Error en getMonitoringsWithAssignedMonitorsByUserIdAndRole: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor al procesar la solicitud.");
        }
    }
    
    @RequestMapping(value= "/findByFaculty", method = RequestMethod.POST)
    public ResponseEntity<?> getAllMonitoringPerSchool(@RequestBody MonitoringDTO monitoringDTO){
        try{
            List<Monitoring> listMonitoring = monitoringService.findBySchool(monitoringDTO);
            if(!listMonitoring.isEmpty()){
                return ResponseEntity.status(200).body(listMonitoring);
            }

            return ResponseEntity.status(400).body("No hay monitorias en la lista");
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @RequestMapping(value= "/findByProgram", method = RequestMethod.POST)
    public ResponseEntity<?> getAllMonitoringPerProgram(@RequestBody MonitoringDTO monitoringDTO){
        try{
            List<Monitoring> listMonitoring = monitoringService.findByProgram(monitoringDTO);
            if(!listMonitoring.isEmpty()){
                return ResponseEntity.status(200).body(listMonitoring);
            }

            return ResponseEntity.status(400).body("No hay monitorias en la lista");
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @RequestMapping(value= "/findByCourse", method = RequestMethod.POST)
    public ResponseEntity<?> getAllMonitoringPerCourse(@RequestBody MonitoringDTO monitoringDTO){
        try{
            List<Monitoring> listMonitoring = monitoringService.findByCourse(monitoringDTO);
            if(!listMonitoring.isEmpty()){
                return ResponseEntity.status(200).body(listMonitoring);
            }

            return ResponseEntity.status(400).body("No hay monitorias en la lista");
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

   @RequestMapping(value= "/createAll/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> createMultipleMonitoring(@RequestParam("file") MultipartFile file, @PathVariable String id){
       try {
           System.out.println("Inside createMultipleMonitoring");
            return ResponseEntity.status(200).body(monitoringService.processListMonitor(file,id));
        } catch (Exception e) {
           System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Error al procesar el archivo: " + e.getMessage());
        }

    }

    @RequestMapping(value= "/profile/{id}/{role}", method = RequestMethod.GET)
    public ResponseEntity<?> monitoringToProfile(@PathVariable String id, @PathVariable String role){
        try {
            if(role.equalsIgnoreCase("professor"))
                return ResponseEntity.status(200).body(monitoringService.getByProfessor(id));
            else if(role.equalsIgnoreCase("monitor"))
                return ResponseEntity.status(200).body(monitoringService.getByMonitor(id));
            else
                return ResponseEntity.status(200).body(monitoringService.getByHeadDepartment(id));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }

    @RequestMapping(value= "/getMonitorsReport/{idProfessor}/{role}", method = RequestMethod.GET)
    public ResponseEntity<?> getMonitorsReport(@PathVariable String idProfessor, @PathVariable String role){
        try{

            return ResponseEntity.status(200).body(monitoringService.getReportMonitors(idProfessor, role));

        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @RequestMapping(value= "/getProfessorReport/{idProfessor}", method = RequestMethod.GET)
    public ResponseEntity<?> getProfessorReport(@PathVariable String idProfessor){
        try{
            return ResponseEntity.status(200).body(monitoringService.getProfessorReport(idProfessor));

        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @GetMapping("/getCategoriesReport/professor/{professorId}")
    public ResponseEntity<?> getProfessorCategoriesReport(
            @PathVariable String professorId,
            @RequestParam(required = false) Long monitoringId) {
        try {
            Optional<Long> optionalMonitoringId = Optional.ofNullable(monitoringId);
            Map<String, Object> reportData = monitoringService.getCategoryReport(professorId, optionalMonitoringId);

            List<?> details = (List<?>) reportData.getOrDefault("detalle_por_curso", Collections.emptyList());
            List<?> totals = (List<?>) reportData.getOrDefault("totales_por_categoria", Collections.emptyList());

            if (details.isEmpty() && totals.isEmpty() && !reportData.containsKey("message")) { 
                System.out.println("Reporte de categorías vacío generado para profesor: " + professorId + ", monitoría: " + optionalMonitoringId);
            }
            return ResponseEntity.ok(reportData);

        } catch (Exception e) {
            if (e.getMessage().contains("no encontrado") || e.getMessage().contains("no pertenece")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            }
            System.err.println("Error en getProfessorCategoriesReport: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "Error interno al generar el reporte de categorías para el profesor."));
        }
    }

    @GetMapping("/getCategoriesReport/jfedpto/{departmentHeadId}") 
    public ResponseEntity<?> getDepartmentHeadCategoriesReport(
            @PathVariable String departmentHeadId,
            @RequestParam(required = false) Long monitoringId) {
        try {
            Optional<Long> optionalMonitoringId = Optional.ofNullable(monitoringId);
            Map<String, Object> reportData = monitoringService.getDepartmentCategoryReport(departmentHeadId, optionalMonitoringId);

            List<?> details = (List<?>) reportData.getOrDefault("detalle_por_curso", Collections.emptyList());
            List<?> totals = (List<?>) reportData.getOrDefault("totales_por_categoria", Collections.emptyList());

            if (details.isEmpty() && totals.isEmpty() && !reportData.containsKey("message")) {
                 System.out.println("Reporte de categorías vacío generado para jefe de departamento: " + departmentHeadId + ", monitoría: " + optionalMonitoringId);
            }
            return ResponseEntity.ok(reportData);

        } catch (Exception e) {
            if (e.getMessage().contains("no encontrado") || e.getMessage().contains("no pertenece") || e.getMessage().contains("Jefe de departamento")) { 
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            }
            System.err.println("Error en getDepartmentHeadCategoriesReport: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "Error interno al generar el reporte de categorías para el jefe de departamento."));
        }
    }

    // @GetMapping("/getAttendanceReport/{professorId}")
    // public ResponseEntity<?> getProfessorMonthlyAttendance(@PathVariable String professorId,@RequestParam(required = false) Long monitoringId) {
    //     try {
    //         Optional<Long> optionalMonitoringId = Optional.ofNullable(monitoringId);
    //         List<Map<String, Object>> reportData = monitoringService.getMonthlyAttendanceReport(professorId, optionalMonitoringId);

    //         if (reportData.isEmpty()) {
    //              return ResponseEntity.ok(Map.of("message", "No se encontraron datos de asistencia para los criterios seleccionados.", "data", reportData));

    //         }
    //         return ResponseEntity.ok(reportData);

    //     } catch (Exception e) {
    //          if (e.getMessage().contains("no encontrado") || e.getMessage().contains("no pertenece")) {
    //              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    //          }
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al generar el reporte de asistencia: " + e.getMessage()));
    //     }
    // }

    @GetMapping("/getAttendanceReport/{role}/{userId}")
    public ResponseEntity<?> getAttendanceReportByRole(
            @PathVariable String role,
            @PathVariable String userId,
            @RequestParam(required = false) Long monitoringId) {
        try {
            Optional<Long> optionalMonitoringId = Optional.ofNullable(monitoringId);
            List<Map<String, Object>> reportData;

            if ("professor".equalsIgnoreCase(role)) {
                reportData = monitoringService.getMonthlyAttendanceReport(userId, optionalMonitoringId);
            } else if ("jfedpto".equalsIgnoreCase(role)) { 
                reportData = monitoringService.getDepartmentMonthlyAttendanceReport(userId, optionalMonitoringId);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Rol no válido proporcionado: " + role));
            }
            if (reportData.isEmpty()) {
                // return ResponseEntity.ok(Map.of("message", "No se encontraron datos de asistencia para los criterios seleccionados.", "data", reportData));
                System.out.println("Reporte de asistencia vacío generado para usuario: " + userId + ", rol: " + role);
            }
            return ResponseEntity.ok(reportData);

        } catch (Exception e) {
            if (e.getMessage().contains("no encontrado") || e.getMessage().contains("no pertenece") || e.getMessage().contains("Jefe de departamento") || e.getMessage().contains("Profesor con ID")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            }
            System.err.println("Error en getAttendanceReportByRole para usuario " + userId + ", rol " + role + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of("error", "Error interno al generar el reporte de asistencia: " + e.getMessage()));
        }
    }

    @RequestMapping(value= "/deleteMonitoring/{idMonitoring}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMonitoring(@PathVariable String idMonitoring){
        try{
            return ResponseEntity.status(200).body(monitoringService.deleteMonitoring(Long.parseLong(idMonitoring)));

        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

}
