package com.example.BarclaysTest.model;

import jakarta.validation.constraints.NotBlank;

public enum TransactionType {
    DEPOSIT("deposit"), WITHDRAWAL("withdrawal");

    @NotBlank
    private String value;


    TransactionType(String val) {
        this.value = val;
    }

    public String getValue(){
        return value;
    }
}
