package com.example.BarclaysTest.model.Authentication;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    @NotNull
    @Pattern(regexp = "usr-[A-Za-z0-9]+", message = "unique id")
    public String id;
}
