package com.example.BarclaysTest.model.Requests;

import com.example.BarclaysTest.model.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class UpdateUserRequest {
    private String name;
    @Valid
    private Address address;
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in the format +1234567890")
    private String phoneNumber;
    @Email
    private String email;
}
