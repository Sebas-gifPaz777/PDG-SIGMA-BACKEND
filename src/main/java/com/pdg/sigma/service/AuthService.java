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

    private final WebClient webClientPrimary;
    private final WebClient webClientFallback;

    public AuthService(WebClient.Builder webClientBuilder) {
        this.webClientPrimary = webClientBuilder.baseUrl("https://api-banner.onrender.com").build();
        this.webClientFallback = webClientBuilder.baseUrl("http://localhost:5431").build();
    }
    
    public Mono<String> getAuthData(AuthDTO authDTO) {
        return webClientPrimary.post()
                .uri("/api/auth/login")
                .bodyValue(authDTO)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSubscribe(subscription -> System.out.println("Iniciando conexión con WebClient PRIMARY..."))
                .doOnRequest(n -> System.out.println("Enviando solicitud al WebClient PRIMARY..."))
                .doOnNext(response -> System.out.println("Respuesta recibida del WebClient PRIMARY"))
                .doOnError(e -> System.out.println("Error al consumir WebClient PRIMARY: " + e.getMessage()))
                .onErrorResume(e -> {
                    System.out.println("Fallo WebClient PRIMARY, intentando con WebClient FALLBACK (localhost)...");
                    return webClientFallback.post()
                            .uri("/api/auth/login")
                            .bodyValue(authDTO)
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnSubscribe(sub -> System.out.println("Intentando conexión con WebClient FALLBACK..."))
                            .doOnNext(response -> System.out.println("Respuesta recibida del WebClient FALLBACK"))
                            .doOnError(err -> System.out.println("Error también en WebClient FALLBACK: " + err.getMessage()));
                });
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

    public String authAPI(String id, String password) throws Exception {
        AuthDTO authDTO = new AuthDTO(id, password);
        String respuesta = getAuthData(authDTO).block();
        return respuesta;
    }
    
}