package com.example.BarclaysTest.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@Builder
public class Address {

    @NotBlank
    private String addressLine1;
    @NotBlank
    private String town;
    @NotBlank
    private String county;
    @NotBlank
    private String postCode;
    private String addressLine2;
    private String addressLine3;

}
