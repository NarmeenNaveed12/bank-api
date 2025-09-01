package com.example.BarclaysTest.model.Authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AuthResponse {
    private String jwtToken;

    public AuthResponse(String token) {
        this.jwtToken = token;
    }
}
