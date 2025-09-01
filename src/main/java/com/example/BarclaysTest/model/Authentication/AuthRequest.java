package com.example.BarclaysTest.model.Authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthRequest {
    @NotBlank
    @Pattern(regexp = "usr-[A-Za-z0-9]+", message = "unique id")
    private String id;
}
