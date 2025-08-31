package com.example.BarclaysTest.ExceptionHandler;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String message){
        super(message);
    }
}
