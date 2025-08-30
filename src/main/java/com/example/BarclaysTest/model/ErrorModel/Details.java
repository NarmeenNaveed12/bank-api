package com.example.BarclaysTest.model.ErrorModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Details {
    private String field;
    private String message;
    private String type;

    public Details(String field, String message, String type) {
        this.field = field;
        this.message = message;
        this.type = type;
    }

}
