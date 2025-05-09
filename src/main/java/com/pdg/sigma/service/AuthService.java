package com.pdg.sigma.service;

import java.util.Optional;

import com.pdg.sigma.util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.pdg.sigma.domain.DepartmentHead;
import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.domain.Prospect;
import com.pdg.sigma.dto.AuthDTO;
import com.pdg.sigma.repository.DepartmentHeadRepository;
import com.pdg.sigma.repository.MonitorRepository;
import com.pdg.sigma.repository.ProfessorRepository;
import com.pdg.sigma.repository.ProspectRepository;

import reactor.core.publisher.Mono;

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

    @Autowired
    private JwtService jwtService;

    private final WebClient webClient;

    public AuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api-banner-production.up.railway.app").build();
        //this.webClient = webClientBuilder.baseUrl("http://localhost:5431").build();
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
                    String token = jwtService.generateToken(auth.getUserId(),"monitor");
                    return new AuthDTO("monitor", token, 1);
                }
            }
            if(response.equalsIgnoreCase("professor")){
                if(professor.isPresent()){
                    String token = jwtService.generateToken(auth.getUserId(),response);
                    return new AuthDTO(response, token,1);
                }
                else
                    throw new Exception("Este profesor no tiene materias asignadas dentro del sistema");
            }
            String token = jwtService.generateToken(auth.getUserId(),response);
            return new AuthDTO(response, token,1);
        }
        else
            throw new Exception("No hay un usuario con este id o contraseña");
    }

   /* public String authAPI(String id, String password) throws Exception {
        AuthDTO authDTO = new AuthDTO(id, password);
        String respuesta = getAuthData(authDTO).block();
        return respuesta;
    }*/

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