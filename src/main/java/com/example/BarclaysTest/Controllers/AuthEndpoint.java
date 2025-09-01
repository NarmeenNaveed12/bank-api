package com.example.BarclaysTest.Controllers;

import com.example.BarclaysTest.ExceptionHandler.NotFoundException;
import com.example.BarclaysTest.Service.UserService;
import com.example.BarclaysTest.model.Authentication.AuthRequest;
import com.example.BarclaysTest.model.Authentication.AuthResponse;
import com.example.BarclaysTest.model.ErrorModel.ErrorResponse;
import com.example.BarclaysTest.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")  // base path for this controller
@Tag(name = "auth")
public class AuthEndpoint {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Authenticate a new user")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest){
        String userId = authRequest.getId();
        if(!userService.isUserCreated(userId)){
            throw new NotFoundException("User not found");
        }
           String token = "Bearer " + jwtUtil.generateToken(userId);
           return ResponseEntity.ok(new AuthResponse(token));
    }
}
