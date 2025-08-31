package com.example.BarclaysTest.model.Requests;

import com.example.BarclaysTest.model.Address;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class UpdateUserRequest {
    private String name;
    @NotNull
    private Address address;
    @Pattern(regexp = "^\\+\\d{10,15}$", message = "Phone number must be in the format +1234567890")
    private String phoneNumber;
    @Email
    private String email;
}
