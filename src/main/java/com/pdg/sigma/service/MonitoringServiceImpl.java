package com.pdg.sigma.service;

import com.pdg.sigma.domain.*;
import com.pdg.sigma.dto.MonitoringDTO;
import com.pdg.sigma.repository.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class MonitoringServiceImpl implements MonitoringService{

    @Autowired
    private MonitoringRepository monitoringRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    public List<Monitoring> findAll() {
        return monitoringRepository.findAll();
    }

    @Override
    public Optional<Monitoring> findById(Long aLong) {
        return monitoringRepository.findById(aLong);
    }

    @Override
    public Monitoring save(Monitoring entity) throws Exception {
        return null;
    }

    @Override
    public Monitoring save(MonitoringDTO entity) throws Exception{
        Program program = programRepository.findByName(entity.getProgramName());
        School school = schoolRepository.findByName(entity.getSchoolName());
        Course course = courseRepository.findByName(entity.getCourseName());
        Monitoring newMonitoring = null;
        Optional<Professor> professor = null;
        if(program.getName().equals(entity.getProgramName()))
            if(school.getName().equals(entity.getSchoolName()))
                if(course.getName().equals(entity.getCourseName()))
                    if(monitoringRepository.findByCourse(course).isEmpty()){
                        professor = professorRepository.findById(entity.getProfessorId());
                        if(professor.isPresent()){
                            newMonitoring = new Monitoring(school,program,course,entity.getStart(),entity.getFinish(), 4.5, 4.5, entity.getSemester(), professor.get());
                            monitoringRepository.save(newMonitoring);
                        }
                        else
                            throw new Exception("El profesor no está registrado");

                        return monitoringRepository.findByCourse(course).get();
                    }
                    else
                        throw new Exception("Ya existe una monitoria para esta materia");
                else
                    throw new Exception("No existe un curso con este nombre");
            else
                throw new Exception("No existe un programa con este nombre");
        else
            throw new Exception("No existe una facultad con este nombre");
    }
    public List<Monitoring> findBySchool(MonitoringDTO monitoringDTO){ //programName = nombre elemento a buscar, courseName = state o estado
        if(!monitoringDTO.getProgramName().isBlank()){
            School entity = schoolRepository.findByName(monitoringDTO.getProgramName());
            System.out.println("Facultad: " + entity.getName());
            List<Monitoring> monitoring = monitoringRepository.findBySchool(entity);
            Date currentDate = new Date();
            if(monitoringDTO.getCourseName().equalsIgnoreCase("Activo") || monitoringDTO.getCourseName().isBlank()){
                List<Monitoring> temp = new ArrayList<>();
                for(Monitoring element: monitoring){
                    if(element.getStart().before(currentDate) ||element.getStart().equals(currentDate) && element.getFinish().after(currentDate) || element.getFinish().equals(currentDate)){
                        temp.add(element);
                    }
                }
                return temp;



            }
            else if(monitoringDTO.getCourseName().equalsIgnoreCase("Inactivo")){
                List<Monitoring> temp = new ArrayList<>();
                for(Monitoring element: monitoring){
                    if(element.getStart().after(currentDate) && element.getFinish().after(currentDate)){
                        temp.add(element);
                    }
                }
                return temp;
            }



            List<Monitoring> temp = new ArrayList<>();
            for(Monitoring element: monitoring){
                if(element.getStart().before(currentDate) && element.getFinish().before(currentDate)){
                    temp.add(element);
                }
            }
            return temp;

        }else{
            List<Monitoring> monitoring = this.findAll();
            Date currentDate = new Date();
            if(monitoringDTO.getCourseName().equalsIgnoreCase("Activo") || monitoringDTO.getCourseName().isBlank()){
                List<Monitoring> temp = new ArrayList<>();
                for(Monitoring element: monitoring){
                    if(element.getStart().before(currentDate) && element.getFinish().after(currentDate)){
                        temp.add(element);
                    }
                }
                return temp;



            }
            else if(monitoringDTO.getCourseName().equalsIgnoreCase("Inactivo")){
                List<Monitoring> temp = new ArrayList<>();
                for(Monitoring element: monitoring){
                    if(element.getStart().after(currentDate) && element.getFinish().after(currentDate)){
                        temp.add(element);
                    }
                }
                return temp;
            }



            List<Monitoring> temp = new ArrayList<>();
            for(Monitoring element: monitoring){
                if(element.getStart().before(currentDate) && element.getFinish().before(currentDate)){
                    temp.add(element);
                }
            }
            return temp;
        }

    }

    public List<Monitoring> findByProgram(MonitoringDTO monitoringDTO) {//programName = nombre elemento a buscar, courseName = state o estado
        Program entity = programRepository.findByName(monitoringDTO.getProgramName());
        List<Monitoring> monitoring = monitoringRepository.findByProgram(entity);

        Date currentDate = new Date();
        if(monitoringDTO.getCourseName().equalsIgnoreCase("Activo") || monitoringDTO.getCourseName().isBlank()){
            List<Monitoring> temp = new ArrayList<>();
            for(Monitoring element: monitoring){
                if(element.getStart().before(currentDate) && element.getFinish().after(currentDate)){
                    temp.add(element);
                }
            }
            return temp;
        }
        else if(monitoringDTO.getCourseName().equalsIgnoreCase("Inactivo")){
            List<Monitoring> temp = new ArrayList<>();
            for(Monitoring element: monitoring){
                if(element.getStart().after(currentDate) && element.getFinish().after(currentDate)){
                    temp.add(element);
                }
            }
            return temp;
        }

        List<Monitoring> temp = new ArrayList<>();
        for(Monitoring element: monitoring){
            if(element.getStart().before(currentDate) && element.getFinish().before(currentDate)){
                temp.add(element);
            }
        }
        return temp;
    }

    public List<Monitoring> findByCourse(MonitoringDTO monitoringDTO) {//programName = nombre elemento a buscar, courseName = state o estado
        Course entity = courseRepository.findByName(monitoringDTO.getProgramName());
        Optional<Monitoring> monitoring = monitoringRepository.findByCourse(entity);
        Date currentDate = new Date();
        if(monitoringDTO.getCourseName().equalsIgnoreCase("Activo") || monitoringDTO.getCourseName().isBlank()){
            List<Monitoring> temp = new ArrayList<>();
            for(Monitoring element: monitoring.stream().toList()){
                if(element.getStart().before(currentDate) && element.getFinish().after(currentDate)){
                    temp.add(element);
                }
            }
            return temp;
        }
        else if(monitoringDTO.getCourseName().equalsIgnoreCase("Inactivo")){
            List<Monitoring> temp = new ArrayList<>();
            for(Monitoring element: monitoring.stream().toList()){
                if(element.getStart().after(currentDate) && element.getFinish().after(currentDate)){
                    temp.add(element);
                }
            }
            return temp;
        }

        List<Monitoring> temp = new ArrayList<>();
        for(Monitoring element: monitoring.stream().toList()){
            if(element.getStart().before(currentDate) && element.getFinish().before(currentDate)){
                temp.add(element);
            }
        }
        return temp;
    }
    @Override
    public Monitoring update(Monitoring entity) throws Exception {
        return null;
    }

    @Override
    public void delete(Monitoring entity) throws Exception {

    }

    @Override
    public void deleteById(Long aLong) throws Exception {

    }

    @Override
    public void validate(Monitoring entity) throws Exception {

    }

    @Override
    public Long count() {
        return null;
    }


    public String processListMonitor(MultipartFile file) throws Exception {

        if (file.isEmpty()) {
            return "El archivo está vacío";
        }

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // Leer la primera hoja


        Map<String, List<List<String>>> dataMap = new HashMap<>();

        int maxColumns = getMaxColumnCount(sheet);
        Row headerRow = sheet.getRow(0);
        List<String> headers = getRowData(headerRow, maxColumns);


        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                List<String> rowData = getRowData(row, maxColumns); // Leer toda la fila
                String key = rowData.size() > maxColumns ? rowData.get(maxColumns) : "UNKNOWN";

                dataMap.computeIfAbsent(key, k -> new ArrayList<>()).add(rowData);
            }
        }

        return "Document reading done";
    }

    private int getMaxColumnCount(Sheet sheet) {
        int maxColumns = 0;
        for (Row row : sheet) {
            maxColumns = Math.max(maxColumns, row.getLastCellNum());
        }
        return maxColumns;
    }

    private List<String> getRowData(Row row, int maxColumns) {
        List<String> rowData = new ArrayList<>();
        for (int i = 0; i < maxColumns; i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            rowData.add(cell.toString());
        }
        return rowData;
    }
}
