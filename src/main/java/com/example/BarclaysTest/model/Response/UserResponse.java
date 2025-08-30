package com.example.BarclaysTest.model.Response;

import com.example.BarclaysTest.model.Address;
import com.example.BarclaysTest.model.BankAccount;
import com.example.BarclaysTest.model.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserResponse {
    @NotNull
    @Pattern(regexp = "usr-[A-Za-z0-9]+")
    public String id;
    @NotBlank
    public String name;
    @Valid
    public Address address;
    @NotBlank
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$")
    public String phoneNumber;
    @NotBlank
    @Email
    public String email;

    public UserResponse(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.address = user.getAddress();
        this.phoneNumber = user.getPhoneNumber();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();

    }
}
