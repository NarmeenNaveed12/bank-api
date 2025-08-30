package com.example.BarclaysTest.model.ErrorModel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BadRequestErrorResponse {
    private String message;
    private List<Details> details;

    public BadRequestErrorResponse(String message) {
        this.message = message;

    }
}
