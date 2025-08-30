package com.example.BarclaysTest.model;

public enum TransactionType {
    DEPOSIT("deposit"), WITHDRAWL("withdrawl");

    private String value;


    TransactionType(String val) {
        this.value = val;
    }

    public String getValue(){
        return value;
    }
}
