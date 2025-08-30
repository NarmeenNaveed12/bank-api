package com.example.BarclaysTest.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {

    @NotBlank
    public String addressLine1;
    @NotBlank
    public String town;
    @NotBlank
    public String county;
    @NotBlank
    public String postCode;

}
