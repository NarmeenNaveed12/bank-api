package com.example.BarclaysTest.ExceptionHandler;

import com.example.BarclaysTest.model.ErrorModel.BadRequestErrorResponse;
import com.example.BarclaysTest.model.ErrorModel.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //exceptions to handle(200, 201, 400, 401, 403, 404, 409, 422, 500)

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unExpectedError(Exception e){
        ErrorResponse error = new ErrorResponse("An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> responseEntityException(ResponseStatusException e){
        ErrorResponse error = new ErrorResponse(e.getReason());
        return ResponseEntity.status(e.getStatusCode()).body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BadRequestErrorResponse> invalidOrMissingData(MethodArgumentNotValidException e, HttpServletRequest request){
      String requestUri = request.getMethod();
        if ("POST".equalsIgnoreCase(requestUri)) {
           BadRequestErrorResponse errorResponse = new BadRequestErrorResponse("Invalid details supplied");
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }
        BadRequestErrorResponse errorResponse = new BadRequestErrorResponse("The request didn't supply all the necessary data");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}
