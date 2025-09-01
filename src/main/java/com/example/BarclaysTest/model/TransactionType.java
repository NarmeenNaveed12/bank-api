package com.example.BarclaysTest.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionType {
    DEPOSIT("deposit"), WITHDRAWAL("withdrawal");

    private String value;

    TransactionType(String val) {
        this.value = val;
    }

    @JsonValue
    public String getValue(){
        return value;
    }
}
