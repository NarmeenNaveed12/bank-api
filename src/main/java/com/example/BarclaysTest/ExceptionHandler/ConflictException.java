package com.example.BarclaysTest.ExceptionHandler;

public class ConflictException extends RuntimeException{
    public ConflictException(String message){
        super(message);
    }
}
