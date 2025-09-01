package com.example.BarclaysTest.model.Requests;

import com.example.BarclaysTest.model.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    @NotBlank
    private String name;
    @NotNull
    @Valid
    private Address address;
    @NotBlank
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in the format +1234567890")
    private String phoneNumber;
    @NotBlank
    @Email
    private String email;
}
