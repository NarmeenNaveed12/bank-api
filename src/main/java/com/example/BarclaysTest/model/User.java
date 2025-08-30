package com.example.BarclaysTest.model;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class User {
    @Pattern(regexp = "usr-[A-Za-z0-9]+", message = "unique id")
    public String id;
    public String name;
    public Address address;
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "unique id")
    public String phoneNumber;
    public String email;

}
