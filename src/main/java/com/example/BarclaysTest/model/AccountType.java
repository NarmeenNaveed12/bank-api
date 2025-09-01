package com.example.BarclaysTest.model;

public enum AccountType {

    PERSONAL("personal");

    private String value;

    AccountType(String personal) {
        this.value = personal;
    }

    public String getValue(){
        return value;
    }
}
