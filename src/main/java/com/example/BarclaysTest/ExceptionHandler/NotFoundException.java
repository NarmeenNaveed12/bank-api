package com.example.BarclaysTest.ExceptionHandler;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
