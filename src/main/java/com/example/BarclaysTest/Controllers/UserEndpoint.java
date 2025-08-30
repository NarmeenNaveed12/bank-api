package com.example.BarclaysTest.Controllers;

import com.example.BarclaysTest.Service.UserService;
import com.example.BarclaysTest.model.ErrorModel.BadRequestErrorResponse;
import com.example.BarclaysTest.model.ErrorModel.ErrorResponse;
import com.example.BarclaysTest.model.Requests.CreateUserRequest;
import com.example.BarclaysTest.model.Response.UserResponse;
import com.example.BarclaysTest.model.User;
import com.example.BarclaysTest.util.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")  // base path for this controller
@Validated //to allow patern to work
public class UserEndpoint {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;


    @PostMapping
    @Description("Create a new user")
    //keeping return type generic as to allow either to return userresponse or to return error response
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest userRequest){
        ResponseEntity responseEntity = null;
        String userId = userService.generateUserId();
        if(!userService.isUserCreated(userId)) {
            try {
                User user = userService.createUser(userRequest,userId);
                UserResponse response = new UserResponse(user);
                return ResponseEntity.status(HttpStatus.CREATED).body(response); //201 RESPONSE
            } catch (ConstraintViolationException e) {
                // 400 Bad Request
                BadRequestErrorResponse error = new BadRequestErrorResponse("Invalid details supplied");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            } catch (Exception e) {
                // 500
                ErrorResponse error = new ErrorResponse("An unexpected error occurred");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
        }

        return responseEntity;
    }





    @GetMapping("/{userId}")
    @Description("Fetch user by ID")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> fetchUserByID(
            @PathVariable @Pattern(regexp = "usr-[A-Za-z0-9]+") String userId) throws Exception {
      // when v1/auth ran, principal = userid from token
        String authenticatedUserId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        ResponseEntity responseEntity = null;
        User user = userService.getUserIfAuthorized(userId,authenticatedUserId);
        try {
            UserResponse userResponse = new UserResponse(user);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(userResponse);
        } catch (ConstraintViolationException e) {
            BadRequestErrorResponse error = new BadRequestErrorResponse("The request didn't supply all the necessary data");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            // 500
            ErrorResponse error = new ErrorResponse("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

        return responseEntity;
    }



}
