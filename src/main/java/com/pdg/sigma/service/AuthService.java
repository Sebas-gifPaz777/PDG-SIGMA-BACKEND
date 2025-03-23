package com.pdg.sigma.service;

import com.pdg.sigma.domain.Monitor;
import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.domain.Prospect;
import com.pdg.sigma.dto.AuthDTO;
import com.pdg.sigma.repository.MonitorRepository;
import com.pdg.sigma.repository.ProfessorRepository;
import com.pdg.sigma.repository.ProspectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ProspectRepository prospectRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private MonitorRepository monitorRepository;
    public AuthDTO loginUser(AuthDTO auth) throws Exception{
        Optional<Prospect> student = prospectRepository.findById(auth.getUserId());
        Optional<Professor> professor = professorRepository.findById(auth.getUserId());
        Optional<Monitor> monitor = monitorRepository.findByIdMonitor(auth.getUserId());

        if(monitor.isPresent()){
            if(student.get().getPassword().equals(auth.getPassword())){
                System.out.println("Dentro de monitor");
                return new AuthDTO( "monitor");
            }
            else
                throw new Exception("Constraseña Invalida");
        }
        if(student.isPresent()){
            if(student.get().getPassword().equals(auth.getPassword())){
                return new AuthDTO( "student");
            }
            else
                throw new Exception("Constraseña Invalida");
        }
        else if(professor.isPresent()) {

            if(professor.get().getPassword().equals(auth.getPassword())){
                return new AuthDTO( "professor");
            }
            else
                throw new Exception("Constraseña Invalida");

        }
        else
            throw new Exception("No hay un usuario con este id");
    }
}
