package com.example.BarclaysTest.model.ErrorModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    public ErrorResponse(String message) {
        this.message = message;
    }


}
