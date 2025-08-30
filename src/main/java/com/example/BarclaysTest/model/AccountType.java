package com.example.BarclaysTest.model;

import jakarta.validation.constraints.NotBlank;

public enum AccountType {

    PERSONAL("personal");

    @NotBlank
    private String value;

    AccountType(String personal) {
        this.value = personal;
    }

    public String getValue(){
        return value;
    }
}
