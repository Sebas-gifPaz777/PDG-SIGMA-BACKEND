package com.pdg.sigma.controller;

import com.pdg.sigma.dto.AuthDTO;
import com.pdg.sigma.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//@CrossOrigin(origins = "https://pdg-sigma.vercel.app/")
//@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value= "/login", method =RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody AuthDTO auth){
        try{
            return ResponseEntity.ok(authService.loginUser(auth));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }
}