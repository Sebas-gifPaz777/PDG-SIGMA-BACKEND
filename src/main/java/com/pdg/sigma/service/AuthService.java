package com.pdg.sigma.service;

import com.pdg.sigma.config.WebClientConfig;
import com.pdg.sigma.domain.DepartmentHead;
import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.domain.Prospect;
import com.pdg.sigma.dto.AuthDTO;
import com.pdg.sigma.repository.DepartmentHeadRepository;
import com.pdg.sigma.repository.MonitorRepository;
import com.pdg.sigma.repository.ProfessorRepository;
import com.pdg.sigma.repository.ProspectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AuthService {

    @Autowired
    private ProspectRepository prospectRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private MonitorRepository monitorRepository;

    @Autowired
    private DepartmentHeadRepository departmentHeadRepository;

    private final WebClient webClient;

    public AuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:5432").build(); // Ajusta la URL según corresponda
    }

    public AuthDTO loginUser(AuthDTO auth) throws Exception{
        Optional<Prospect> student = prospectRepository.findById(auth.getUserId());
        Optional<Professor> professor = professorRepository.findById(auth.getUserId());
        Optional<Monitor> monitor = monitorRepository.findByIdMonitor(auth.getUserId());
        Optional<DepartmentHead> departmentHead = departmentHeadRepository.findById(auth.getUserId());
        String response = authAPI(auth.getUserId(), auth.getPassword());

        if(!response.equalsIgnoreCase("false all") && !response.equalsIgnoreCase("false")){
            if(response.equalsIgnoreCase("student")){
                if(monitor.isPresent()){
                    return new AuthDTO("monitor");
                }
            }
            if(response.equalsIgnoreCase("professor")){
                if(professor.isPresent()){
                    return new AuthDTO(response);
                }
                else
                    throw new Exception("Este profesor no tiene materias asignadas dentro del sistema");
            }
            return new AuthDTO(response);
        }
        else
            throw new Exception("No hay un usuario con este id o contraseña");
    }

    public String authAPI(String id, String password) throws Exception{
        AuthDTO authDTO = new AuthDTO(id,password);
        String respuesta = webClient.post()
                .uri("/api/auth/login")
                .bodyValue(authDTO)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return respuesta;
    }
}
