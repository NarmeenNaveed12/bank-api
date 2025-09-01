package com.example.BarclaysTest.model.Authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor //testing
public class AuthRequest {
    @NotBlank
    @Pattern(regexp = "usr-[A-Za-z0-9]+", message = "unique id")
    public String id;
}
