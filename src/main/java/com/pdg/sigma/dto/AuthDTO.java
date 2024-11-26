package com.pdg.sigma.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthDTO {
    private String userId;
    private String password;
    private String role;

    public AuthDTO(String userId, String password, String role){
        this.userId = userId;
        this.password = password;
        this.role = role;
    }
    public AuthDTO(String userId, String password){
        this.userId = userId;
        this.password = password;
    }

    public AuthDTO(String role){
        this.role = role;
    }

}
