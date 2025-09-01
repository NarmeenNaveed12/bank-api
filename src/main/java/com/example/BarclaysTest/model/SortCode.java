package com.example.BarclaysTest.model;

public enum SortCode {
    TEN_TEN_TEN("10-10-10");

    private String value;

    SortCode(String s) {
        this.value = s;
    }

    public String getValue(){
        return value;
    }
}
