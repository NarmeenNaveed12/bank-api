package com.example.BarclaysTest.model;

import jakarta.validation.constraints.NotBlank;

public enum SortCode {
    TEN_TEN_TEN("10-10-10");

    @NotBlank
    private String value;

    SortCode(String s) {
        this.value = s;
    }

    public String getValue(){
        return value;
    }
}
