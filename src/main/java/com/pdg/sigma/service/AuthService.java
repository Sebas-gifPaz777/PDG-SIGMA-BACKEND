package com.pdg.sigma.service;

import com.pdg.sigma.domain.Professor;
import com.pdg.sigma.domain.Student;
import com.pdg.sigma.dto.AuthDTO;
import com.pdg.sigma.repository.ProfessorRepository;
import com.pdg.sigma.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;
    public AuthDTO loginUser(AuthDTO auth) throws Exception{
        Optional<Student> student = studentRepository.findById(auth.getUserId());
        Optional<Professor> professor = professorRepository.findById(auth.getUserId());

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
