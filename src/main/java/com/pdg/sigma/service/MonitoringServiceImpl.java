package com.pdg.sigma.service;

import com.pdg.sigma.domain.*;
import com.pdg.sigma.dto.*;
import com.pdg.sigma.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private MonitoringMonitorRepository monitoringMonitorRepository;

    @Autowired
    private MonitorRepository monitorRepository;

    @Autowired
    private HeadProgramRepository headProgramRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private DepartmentHeadRepository departmentHeadRepository;
    @Autowired
    private CourseProfessorRepository courseProfessorRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;


    @Override
    public List<Monitoring> findAll() {
        Date now = new Date();

        return monitoringRepository.findAll().stream()
                .sorted(Comparator.comparing(m -> {
                    Date start = m.getStart();
                    Date end = m.getFinish();

                    if ((start.before(now) || start.equals(now)) && (end.after(now) || end.equals(now))) {
                        return 0;
                    } else if (start.after(now) && end.after(now)) {
                        return 1;
                    } else {
                        return 2;
                    }
                }))
                .collect(Collectors.toList());
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
        Program program = programRepository.findByName(entity.getProgramName()).get();
        School school = schoolRepository.findByName(entity.getSchoolName()).get();
        Course course = courseRepository.findByName(entity.getCourseName()).get();
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
            School entity = schoolRepository.findByName(monitoringDTO.getProgramName()).get();
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
        Program entity = programRepository.findByName(monitoringDTO.getProgramName()).get();
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
        Course entity = courseRepository.findByName(monitoringDTO.getProgramName()).get();
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

    public List<MonitoringDTO> getByProfessor(String id) throws Exception{
        Optional<Professor> professor = professorRepository.findById(id);
        if(professor.isPresent()){
            List<Monitoring> monitorings = monitoringRepository.findByProfessor(professor.get());
            List<MonitoringDTO> monitoringDTOs = new ArrayList<>();

            if(!monitorings.isEmpty()){
                for(Monitoring monitoring:monitorings){
                    List<MonitoringMonitor> list = monitoringMonitorRepository.findByMonitoring(monitoring);
                    List<Monitor> monitors = new ArrayList<>();
                    String name ="";

                    if(!list.isEmpty()){
                        for(MonitoringMonitor monitoringMonitor:list){
                            name = name+monitoringMonitor.getMonitor().getName()+" "+monitoringMonitor.getMonitor().getLastName()+", ";
                        }
                        name = name.replaceAll(", $", "");
                        monitoringDTOs.add(new MonitoringDTO(monitoring.getId(), monitoring.getCourse().getName(), monitoring.getStart(), monitoring.getFinish(), monitoring.getSemester(), name));
                    }
                    else{
                        monitoringDTOs.add(new MonitoringDTO(monitoring.getId(), monitoring.getCourse().getName(), monitoring.getStart(), monitoring.getFinish(), monitoring.getSemester(), "N/A"));
                    }
                }

                return monitoringDTOs;
            }
            else
                throw new Exception("No tiene monitorias creadas");
        }
        else
            throw new Exception("No existe un profesor con este ID");
    }

    public List<MonitoringDTO> getByMonitor(String id) throws Exception{
        Optional<Monitor> monitor = monitorRepository.findByIdMonitor(id);
        if(monitor.isPresent()){
            List<MonitoringMonitor> monitoringMonitors = monitoringMonitorRepository.findByMonitor(monitor.get());
            List<Monitoring> monitorings = new ArrayList<>();
            for(MonitoringMonitor monitoringMonitor:monitoringMonitors){
                monitorings.add(monitoringMonitor.getMonitoring());
            }
            List<MonitoringDTO> monitoringDTOs = new ArrayList<>();

            if(!monitorings.isEmpty()){
                for(Monitoring monitoring:monitorings){
                    monitoringDTOs.add(new MonitoringDTO(monitoring.getId(), monitoring.getCourse().getName(), monitoring.getStart(), monitoring.getFinish(), monitoring.getSemester(), monitoring.getProfessor().getName()));
                }
                System.out.println(monitoringDTOs.get(0).getMonitor());

                return monitoringDTOs;
            }
            else
                throw new Exception("No tiene monitorias creadas");
        }
        else
            throw new Exception("No existe un monitor con este ID");
    }

    public List<ReportDTO>getReportMonitors(String idProfessor, String role) throws Exception{
        if(role.equalsIgnoreCase("professor")){
            Optional<Professor> professor = professorRepository.findById(idProfessor);
            if(professor.isEmpty()){
                throw new Exception("No hay un profesor con este Id");
            }
            List<Monitoring> monitorings = monitoringRepository.findByProfessor(professor.get());

            if(monitorings.isEmpty()){
                throw new Exception("No hay monitorias creadas");
            }
            List<MonitoringMonitor> monitors = new ArrayList<>();
            for(Monitoring monitoring:monitorings){
                monitors.addAll(monitoringMonitorRepository.findByMonitoring(monitoring));
            }
            if( monitors.isEmpty()){
                throw new Exception("No hay reportes por mostrar");
            }

            List<ReportDTO> reportDTOList = new ArrayList<>();
            for(MonitoringMonitor monitor:monitors){
                List<Activity> activities = filterAssigned(activityRepository.findByMonitorAndRoleResponsable(monitor.getMonitor(), "M"),
                        activityRepository.findByMonitorAndRoleCreator(monitor.getMonitor(), "M"));

                ReportDTO reportDTO = new ReportDTO(0,0,0);
                if(!activities.isEmpty()){
                    for(Activity activity:activities){
                        if(activity.getMonitoring().equals(monitor.getMonitoring())){
                            if(activity.getState().equals(StateActivity.PENDIENTE))
                                reportDTO.setPending(reportDTO.getPending()+1);

                            if(activity.getState().equals(StateActivity.COMPLETADO))
                                reportDTO.setCompleted(reportDTO.getCompleted()+1);

                            if(activity.getState().equals(StateActivity.COMPLETADOT))
                                reportDTO.setLate(reportDTO.getLate()+1);
                        }
                    }
                    reportDTO.setName(monitor.getMonitor().getName());
                    reportDTO.setCourse(monitor.getMonitoring().getCourse().getName());
                    reportDTO.setProfessor(professor.get().getName());
                    reportDTO.setSemester(monitor.getMonitoring().getSemester());
                    reportDTO.setProgram(monitor.getMonitoring().getCourse().getProgram().getName());
                    reportDTO.setIdProfessor(professor.get().getId());
                    String[] nameCourse = monitor.getMonitoring().getCourse().getName().split(" ");
                    if(nameCourse.length>2){
                        reportDTO.setNameAndCourse(monitor.getMonitor().getName()+" - "+nameCourse[0]+" "+nameCourse[1]+"...");
                    }else{
                        reportDTO.setNameAndCourse(monitor.getMonitor().getName()+" - "+monitor.getMonitoring().getCourse().getName());
                    }
                    reportDTOList.add(reportDTO);
                }
            }
            if(!reportDTOList.isEmpty()){
                return reportDTOList;
            }else
                throw new Exception("No hay reportes por mostrar");
        }
        Optional<DepartmentHead> hd = departmentHeadRepository.findById(idProfessor);
        if(hd.isEmpty()){
            throw new Exception("No existe un jefe con este Id");
        }
        List<HeadProgram> hp = headProgramRepository.findByDepartmentHeadId(hd.get().getId());

        if(hp.isEmpty()){
            throw new Exception("No existe un programa al que este asociado con este Id");
        }

        HeadProgram headProgram = hp.get(0);
        List<Course> courses = courseRepository.findByProgram(headProgram.getProgram());
        for(Course course:courses){
            System.out.println(course.getId());
        }
        if(courses.isEmpty()){
            throw new Exception("No existe un cursos con este programa");
        }
        List<ReportDTO> reportDTOList = new ArrayList<>();
        for(Course course:courses){
            List<CourseProfessor> courseProfessors = courseProfessorRepository.findByCourseId(course.getId());
            System.out.println("Clases "+ courseProfessors.get(0).getCourse().getName());
            if(courseProfessors.isEmpty()){
                throw new Exception("No existe un curso con este Id");
            }
            for(CourseProfessor courseProfessor:courseProfessors){
                Professor professor = courseProfessor.getProfessor();
                System.out.println(professor.getName());
                List<Monitoring> monitorings = monitoringRepository.findByProfessor(professor);

                if(monitorings.isEmpty()){
                    throw new Exception("No hay monitorias creadas");
                }
                List<MonitoringMonitor> monitors = new ArrayList<>();
                for(Monitoring monitoring:monitorings){
                    if(monitoring.getCourse().equals(courseProfessor.getCourse())){
                        monitors.addAll(monitoringMonitorRepository.findByMonitoring(monitoring));
                    }
                }
                if( monitors.isEmpty()){
                    throw new Exception("No hay reportes por mostrar");
                }
                for(MonitoringMonitor monitoring:monitors){
                    System.out.println("Monitorias "+monitoring.getMonitoring().getCourse().getName());
                }


                for(MonitoringMonitor monitor:monitors){
                    List<Activity> activities = filterAssigned(activityRepository.findByMonitorAndRoleResponsable(monitor.getMonitor(), "M"),
                            activityRepository.findByMonitorAndRoleCreator(monitor.getMonitor(), "M"));

                    ReportDTO reportDTO = new ReportDTO(0,0,0);
                    if(!activities.isEmpty()){
                        for(Activity activity:activities){
                            if(activity.getMonitoring().equals(monitor.getMonitoring())){
                                if(activity.getState().equals(StateActivity.PENDIENTE))
                                    reportDTO.setPending(reportDTO.getPending()+1);

                                if(activity.getState().equals(StateActivity.COMPLETADO))
                                    reportDTO.setCompleted(reportDTO.getCompleted()+1);

                                if(activity.getState().equals(StateActivity.COMPLETADOT))
                                    reportDTO.setLate(reportDTO.getLate()+1);
                            }
                        }
                        reportDTO.setName(monitor.getMonitor().getName());
                        reportDTO.setCourse(monitor.getMonitoring().getCourse().getName());
                        reportDTO.setProfessor(professor.getName());
                        reportDTO.setIdProfessor(professor.getId());
                        reportDTO.setSemester(monitor.getMonitoring().getSemester());
                        reportDTO.setProgram(monitor.getMonitoring().getCourse().getProgram().getName());
                        String[] nameCourse = monitor.getMonitoring().getCourse().getName().split(" ");
                        if(nameCourse.length>2){
                            reportDTO.setNameAndCourse(monitor.getMonitor().getName()+" - "+nameCourse[0]+" "+nameCourse[1]+"...");
                        }else{
                            reportDTO.setNameAndCourse(monitor.getMonitor().getName()+" - "+monitor.getMonitoring().getCourse().getName());
                        }
                        reportDTOList.add(reportDTO);
                    }
                }

            }
        }
        if(!reportDTOList.isEmpty()){
            return reportDTOList;
        }else
            throw new Exception("No hay reportes por mostrar");
    }

    public List<ReportDTO> getProfessorReport(String idProfessor)throws Exception {
        Optional<Professor> professor = professorRepository.findById(idProfessor);
        if(professor.isPresent()) {
            List<Monitoring> monitorings = monitoringRepository.findByProfessor(professor.get());

            if (monitorings.isEmpty()) {
                throw new Exception("No hay monitorías creadas");
            }
            List<Activity> activitiesAssigned =filterAssigned(activityRepository.findByProfessorAndRoleResponsable(professor.get(), "P"),
                    activityRepository.findByProfessorAndRoleCreator(professor.get(), "P"));

            List<ReportDTO> reportProfessor = new ArrayList<>();
            for (Monitoring monitoring : monitorings) {
                ReportDTO reportDTO = new ReportDTO(0, 0, 0);
                for (Activity activity : activitiesAssigned) {
                    if (monitoring.equals(activity.getMonitoring())) {
                        switch (activity.getState()) {
                            case StateActivity.PENDIENTE:
                                reportDTO.setPending(reportDTO.getPending() + 1);
                                break;

                            case StateActivity.COMPLETADO:
                                reportDTO.setCompleted(reportDTO.getCompleted() + 1);
                                break;

                            case StateActivity.COMPLETADOT:
                                reportDTO.setLate(reportDTO.getLate() + 1);
                                break;

                            default:
                                throw new Exception("Estado incorrecto");
                        }

                    }
                }

                reportDTO.setName(professor.get().getName());
                reportDTO.setIdProfessor(professor.get().getId());
                reportDTO.setCourse(monitoring.getCourse().getName());
                reportDTO.setProgram(monitoring.getCourse().getProgram().getName());
                reportProfessor.add(reportDTO);
            }
            return reportProfessor;

        }
        else
            throw new Exception("No existe professor con este id");
    }

    public List<Activity> filterAssigned(List<Activity> assigned, List<Activity> creator){
        List<Activity> result = new ArrayList<>(assigned);
        result.removeIf(creator::contains);
        return result;
    }


    public String processListMonitor(MultipartFile file, String professorId) throws Exception {

        List<MonitoringDTO> registList = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Tomar la primera hoja
            Iterator<Row> rowIterator = sheet.iterator();


            if (!rowIterator.hasNext()) {
                throw new Exception("Incompatibilidad con alguno de los campos del archivo");
            }
            // Read headers, columns name
            Row header = rowIterator.next();

            List<String> columnsName = new ArrayList<>();
            for (Cell cell : header) {
                columnsName.add(cell.getStringCellValue().trim());
            }

            if(!checkColumns(columnsName)){
                throw new Exception("Incompatibilidad con alguno de los campos del archivo");
            }


            // Read regist line by line
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();

                MonitoringDTO monitoring = new MonitoringDTO(0.0,0.0); //Initialized monitorings with grades en 0.0
                for (int i = 0; i < columnsName.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String value = getCellValue(cell);

                    if(value.equals("")){
                        throw new Exception("Incompatibilidad con alguno de los campos del archivo");
                    }

                    Object check = checkValue(i, value, monitoring, columnsName);

                    if(check == null){
                        throw new Exception("Incompatibilidad con alguno de los campos del archivo");
                    }

                    monitoring = (MonitoringDTO) check;

                }

                if(!(monitoring.getStart().before(monitoring.getFinish()) || monitoring.getStart().equals(monitoring.getFinish())) &&
                        !(monitoring.getStart().after(new Date()) || monitoring.getStart().equals(new Date()))){

                    throw new Exception("Incompatibilidad con alguno de los campos del archivo");
                }
                String semesterDraft = monitoring.getSemester();

                Date currentDate = monitoring.getStart();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);

                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH) + 1; // Los meses en Calendar van de 0 a 11


                String[] parts = semesterDraft.split("-");
                int givenYear = Integer.parseInt(parts[0]);
                int givenSemester = Integer.parseInt(parts[1]);

                // Si el año no coincide, retorna false
                if (givenYear != currentYear) {
                    throw new Exception("Incompatibilidad con alguno de los campos del archivo (Debe ser el año actual)");
                }


                if (givenSemester == 1) {
                    if(currentMonth > Calendar.JUNE + 1){
                        throw new Exception("Incompatibilidad con alguno de los campos del archivo (Debe ser el semestre actual)");
                    }
                } else if (givenSemester == 2) {
                    if(currentMonth < Calendar.JULY + 1 ){
                        throw new Exception("Incompatibilidad con alguno de los campos del archivo (Debe ser el semestre actual)");
                    }
                }


                registList.add(monitoring);

            }

            for (MonitoringDTO monitoring: registList){
                if(monitoringRepository.findByCourse(monitoring.getCourse()).isPresent()){
                    throw new Exception("Al menos una monitoria está creada");
                }
            }

            Professor professor = professorRepository.findById(professorId).get();

            for (MonitoringDTO monitoring: registList){
                monitoring.setProfessor(professor);
                monitoringRepository.save(new Monitoring(monitoring));
            }
            return "Todas las monitorias han sido creadas";
        }

    }

    //Method to check values header
    private boolean checkColumns(List<String> columns) {
        boolean valid=true;
        for(int i=0; i<columns.size();i++){

            switch (i) {
                case 0:
                    if(!columns.get(0).equalsIgnoreCase("FACULTAD")){
                        valid=false;
                    }
                    break;
                case 1:
                    if(!columns.get(1).equalsIgnoreCase("PROGRAMA")) {
                        valid = false;
                    }
                    break;
                case 2:
                    if(!columns.get(2).equalsIgnoreCase("CURSO")) {
                        valid = false;
                    }
                    break;
                case 3:
                    if(!columns.get(3).equalsIgnoreCase("FECHA INICIO")) {
                        valid = false;
                    }
                    break;
                case 4:
                    if(!columns.get(4).equalsIgnoreCase("FECHA FINALIZACION")) {
                        valid = false;
                    }
                    break;
                case 5:
                    if(!columns.get(5).replaceAll("\\s+$", "").equalsIgnoreCase("PERIODO")) {
                        valid = false;
                    }
                    break;
                case 6:
                    if(!columns.get(6).equalsIgnoreCase("PROMEDIO ACUMULADO") && !columns.get(6).equalsIgnoreCase("PROMEDIO MATERIA")) {
                        valid = false;
                    }
                    break;
                case 7:
                    if(!columns.get(7).equalsIgnoreCase("PROMEDIO MATERIA")) {
                        valid = false;
                    }
                    break;
                default:
                    System.out.println("Opción no disponible"); //Temporal mientras se lleva a producción
            }
        }

        return valid;
    }

    //Method to check type of value
    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    //Method to check value and return object of the value
    private Object checkValue(int column, String value, MonitoringDTO monitoring, List<String> header) throws ParseException {
        String regex = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-\\d{4}$";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String regexSemester = "^\\d{4}-(1|2)$";

        boolean valid=true;
            switch (column) {
                case 0:
                    Optional<School> school = schoolRepository.findByName(value);
                    if(school.isPresent()){
                        monitoring.setSchool(school.get());
                        return monitoring;
                    }
                    else
                        return null;
                case 1:
                    Optional<Program> program = programRepository.findByName(value);
                    if(program.isPresent()){
                        monitoring.setProgram(program.get());
                        return monitoring;
                    }
                    else
                        return null;
                case 2:
                    Optional<Course> course = courseRepository.findByName(value);
                    if(course.isPresent()){
                        monitoring.setCourse(course.get());
                        return monitoring;
                    }
                    else
                        return null;

                case 3:
                    if(value.matches(regex)){
                        monitoring.setStart(formatter.parse(value));
                        return monitoring;
                    }
                    else
                        return null;
                case 4:
                    if(value.matches(regex)){
                        monitoring.setFinish(formatter.parse(value));
                        return monitoring;
                    }
                    else
                        return null;
                case 5:
                    if(value.matches(regexSemester)){

                        monitoring.setSemester(value);
                        return monitoring;
                    }
                    else
                        return null;
                case 6:
                    if(header.get(6).equalsIgnoreCase("PROMEDIO ACUMULADO")){
                        monitoring.setAverageGrade(Double.parseDouble(value));
                    }
                    else{
                        monitoring.setCourseGrade(Double.parseDouble(value));
                    }
                    return monitoring;

                case 7:
                    monitoring.setCourseGrade(Double.parseDouble(value));
                    return monitoring;
                default:
                    System.out.println("Opción no disponible"); //Temporal mientras se lleva a producción
            }


        return valid;
    }

    public List<MonitoringDTO> getByHeadDepartment(String id) throws Exception {
        List<HeadProgram> list = headProgramRepository.findByDepartmentHeadId(id);
        List<MonitoringDTO> monitoringDTOS = new ArrayList<>();
        if(!list.isEmpty()){
            List<Course> courses = courseRepository.findByProgram(list.get(0).getProgram());
            List<Monitoring> monitorings = new ArrayList<>();
            for(Course course:courses){
                Optional<Monitoring> temporal = monitoringRepository.findByCourse(course);
                if(temporal.isPresent()){
                    monitorings.add(temporal.get());
                }
            }
            if(!monitorings.isEmpty()){
                for(Monitoring data: monitorings){
                    List<MonitoringMonitor> monitoringMonitor = monitoringMonitorRepository.findByMonitoring(data);
                    String monitor="";

                    for(MonitoringMonitor value:monitoringMonitor){
                        monitor = value.getMonitor().getName()+" "+value.getMonitor().getLastName()+", ";
                    }
                    monitor.replaceAll(", $", "");

                    monitoringDTOS.add(new MonitoringDTO(data.getId(), data.getCourse().getName(), data.getSemester(), monitor, data.getProfessor().getName()));

                }

                return monitoringDTOS;
            }
            else
                throw new Exception("No hay monitorias creadas");
        }
        else
            throw new Exception("No existe jefe con este id");

    }

    public Map<String, Object> getCategoryReport(String professorId, Optional<Long> optionalMonitoringId) throws Exception {

        Optional<Professor> optionalProfessor = professorRepository.findById(professorId);
        if (optionalProfessor.isEmpty()) {
            throw new Exception("Profesor con ID " + professorId + " no encontrado.");
        }
        Professor professor = optionalProfessor.get();

        List<Monitoring> monitorings;
        if (optionalMonitoringId.isPresent()) {
            Long monitoringId = optionalMonitoringId.get();
            Optional<Monitoring> optionalMonitoring = monitoringRepository.findById(monitoringId);
            if (optionalMonitoring.isEmpty()) {
                throw new Exception("Monitoría con ID " + monitoringId + " no encontrada.");
            }
            Monitoring specificMonitoring = optionalMonitoring.get();
            if (!specificMonitoring.getProfessor().equals(professor)) {
                throw new Exception("La monitoría con ID " + monitoringId + " no pertenece al profesor con ID " + professorId);
            }
            monitorings = List.of(specificMonitoring);
        } else {
            monitorings = monitoringRepository.findByProfessor(professor);
        }

        Map<String, Object> finalReport = new LinkedHashMap<>();
        finalReport.put("detalle_por_curso", Collections.emptyList());
        finalReport.put("totales_por_categoria", Collections.emptyList());


        if (monitorings.isEmpty()) {
            System.out.println("No se encontraron monitorías para los criterios.");
            return finalReport;
        }

        List<Activity> relevantActivities = new ArrayList<>();
        for (Monitoring m : monitorings) {
            if (m.getCourse() != null && m.getCourse().getName() != null && !m.getCourse().getName().trim().isEmpty()) {
                relevantActivities.addAll(
                    activityRepository.findByMonitoring(m).stream()
                            .filter(act -> act.getCategory() != null && !act.getCategory().trim().isEmpty())
                            .collect(Collectors.toList())
                );
            }
        }

        if (relevantActivities.isEmpty()) {
            System.out.println("No se encontraron actividades con categoría y curso válido para las monitorías seleccionadas.");
            return finalReport; 
        }

        Map<String, Map<String, Long>> perCourseCategoryCounts = relevantActivities.stream()
                .collect(Collectors.groupingBy(
                    activity -> activity.getMonitoring().getCourse().getName(),
                    Collectors.groupingBy(
                        Activity::getCategory,
                        Collectors.counting()
                    )
                ));

        Map<String, Long> overallCategoryCounts = relevantActivities.stream()
                .collect(Collectors.groupingBy(
                    Activity::getCategory,
                    Collectors.counting()
                ));

        List<Map<String, Object>> courseDetailsList = new ArrayList<>();
        List<String> sortedCourseNames = perCourseCategoryCounts.keySet().stream().sorted().collect(Collectors.toList());

        for (String courseName : sortedCourseNames) {
            Map<String, Long> categoriesInCourse = perCourseCategoryCounts.get(courseName);
            List<Map<String, Object>> categoryListForCourse = new ArrayList<>();

            List<Map.Entry<String, Long>> sortedCategoriesInCourse = categoriesInCourse.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toList());

            for (Map.Entry<String, Long> categoryEntry : sortedCategoriesInCourse) {
                Map<String, Object> categoryDetail = new HashMap<>();
                categoryDetail.put("categoria", categoryEntry.getKey());
                categoryDetail.put("cantidad", categoryEntry.getValue());
                categoryListForCourse.add(categoryDetail);
                // totalForCourse += categoryEntry.getValue();
            }

            Map<String, Object> courseDetailMap = new LinkedHashMap<>();
            courseDetailMap.put("curso", courseName);
            courseDetailMap.put("categorias", categoryListForCourse);
            // courseDetailMap.put("total_curso", totalForCourse);
            courseDetailsList.add(courseDetailMap);
        }
        
        List<Map<String, Object>> overallCategoryTotalsList = new ArrayList<>();
        // long grandTotalActivities = 0L;
         List<Map.Entry<String, Long>> sortedOverallCategories = overallCategoryCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());

        for (Map.Entry<String, Long> overallEntry : sortedOverallCategories) {
            Map<String, Object> totalCategoryDetail = new HashMap<>();
            totalCategoryDetail.put("categoria", overallEntry.getKey());
            totalCategoryDetail.put("cantidad_total", overallEntry.getValue());
            overallCategoryTotalsList.add(totalCategoryDetail);
            // grandTotalActivities += overallEntry.getValue();
        }

        finalReport.put("detalle_por_curso", courseDetailsList);
        finalReport.put("totales_por_categoria", overallCategoryTotalsList);
        // finalReport.put("total_actividades_general", grandTotalActivities);

        System.out.println("Reporte complejo de categorías generado (sin totales): " + finalReport);
        return finalReport;
    }

    public List<Map<String, Object>> getMonthlyAttendanceReport(String professorId, Optional<Long> optionalMonitoringId) throws Exception {
        Optional<Professor> optionalProfessor = professorRepository.findById(professorId);
        if (optionalProfessor.isEmpty()) {
            throw new Exception("Profesor con ID " + professorId + " no encontrado.");
        }
        Professor professor = optionalProfessor.get();

        List<Monitoring> monitorings;
        if (optionalMonitoringId.isPresent()) {
            Long monitoringId = optionalMonitoringId.get();
            Optional<Monitoring> optionalMonitoring = monitoringRepository.findById(monitoringId);
            if (optionalMonitoring.isEmpty()) {
                throw new Exception("Monitoría con ID " + monitoringId + " no encontrada.");
            }
            Monitoring specificMonitoring = optionalMonitoring.get();
            if (!specificMonitoring.getProfessor().equals(professor)) {
                throw new Exception("La monitoría con ID " + monitoringId + " no pertenece al profesor con ID " + professorId);
            }
            monitorings = List.of(specificMonitoring);
        } else {
            monitorings = monitoringRepository.findByProfessor(professor);
        }

        if (monitorings.isEmpty()) return Collections.emptyList();

        List<Activity> relevantActivities = new ArrayList<>();
        for (Monitoring m : monitorings) {
            if (m.getCourse() != null) {
                relevantActivities.addAll(activityRepository.findByMonitoring(m));
            }
        }

        if (relevantActivities.isEmpty()) return Collections.emptyList();

        List<Attendance> attendances = attendanceRepository.findByActivityIn(relevantActivities);

        if (attendances.isEmpty()) return Collections.emptyList();

        // Agrupar por mes y curso
        Map<YearMonth, Map<String, Map<String, Object>>> groupedData = new HashMap<>();

        for (Attendance attendance : attendances) {
            Activity activity = attendance.getActivity();
            if (activity == null || activity.getDelivey() == null) continue;

            Monitoring monitoring = activity.getMonitoring();
            if (monitoring == null || monitoring.getCourse() == null || monitoring.getCourse().getName() == null) continue;

            String courseName = monitoring.getCourse().getName().trim();
            if (courseName.isEmpty()) continue;

            YearMonth yearMonth = YearMonth.from(activity.getDelivey().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            groupedData.putIfAbsent(yearMonth, new HashMap<>());
            Map<String, Map<String, Object>> courseMap = groupedData.get(yearMonth);

            courseMap.putIfAbsent(courseName, new HashMap<>());
            Map<String, Object> courseData = courseMap.get(courseName);

            // Inicializar lista de estudiantes y contador si no existen
            courseData.putIfAbsent("cantidad", 0L);
            courseData.putIfAbsent("estudiantes", new HashSet<String>());

            // Incrementar contador
            Long currentCount = (Long) courseData.get("cantidad");
            courseData.put("cantidad", currentCount + 1);

            // Agregar estudiante
            Set<String> studentNames = (Set<String>) courseData.get("estudiantes");
            studentNames.add(attendance.getStudent().getName());
        }

        // Ordenar y transformar a lista estructurada
        List<Map<String, Object>> reportList = new ArrayList<>();
        Locale spanishLocale = Locale.forLanguageTag("es-ES");

        groupedData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    YearMonth yearMonth = entry.getKey();
                    Map<String, Map<String, Object>> courseData = entry.getValue();

                    String monthName = Month.of(yearMonth.getMonthValue()).getDisplayName(TextStyle.FULL, spanishLocale);
                    monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);
                    String semester = (yearMonth.getMonthValue() <= 6) ? yearMonth.getYear() + "-1" : yearMonth.getYear() + "-2";

                    Map<String, Object> reportEntry = new LinkedHashMap<>();
                    reportEntry.put("mes", monthName);
                    reportEntry.put("semestre", semester);

                    List<Map<String, Object>> courseDetailsList = new ArrayList<>();
                    long totalForMonth = 0;

                    for (Map.Entry<String, Map<String, Object>> courseEntry : courseData.entrySet()) {
                        String courseName = courseEntry.getKey();
                        Map<String, Object> data = courseEntry.getValue();

                        Long cantidad = (Long) data.get("cantidad");
                        Set<String> estudiantes = (Set<String>) data.get("estudiantes");

                        Map<String, Object> courseDetail = new LinkedHashMap<>();
                        courseDetail.put("curso", courseName);
                        courseDetail.put("cantidad", cantidad);
                        courseDetail.put("estudiantes", estudiantes); // <-- Agregamos nombres

                        courseDetailsList.add(courseDetail);
                        totalForMonth += cantidad;
                    }

                    reportEntry.put("asistencia_por_curso", courseDetailsList);
                    reportEntry.put("total_mes", totalForMonth);
                    reportList.add(reportEntry);
                });

        return reportList;
    }


    public boolean deleteMonitoring(long l) {
        Optional<Monitoring> monitoring = monitoringRepository.findById(l);
        if (!monitoring.isPresent()) {
            return false;
        }
        if(!monitoringMonitorRepository.findByMonitoring(monitoring.get()).isEmpty()) {
            return false;
        }
        monitoringRepository.delete(monitoring.get());
        return true;
    }
}
