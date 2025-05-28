package com.pdg.sigma.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin(origins = "https://pdg-sigma.vercel.app/")
//@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
@RequestMapping("/debug")
public class DebugController {

    @GetMapping("/ip")
    public String getIp() {
        try {
            URL whatIsMyIp = new URL("https://api.ipify.org");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatIsMyIp.openStream()));
            return "Render public IP: " + in.readLine();
        } catch (IOException e) {
            return "Error retrieving IP: " + e.getMessage();
        }
    }
}
