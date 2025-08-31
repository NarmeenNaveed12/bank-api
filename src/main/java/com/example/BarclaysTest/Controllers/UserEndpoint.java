package com.example.BarclaysTest.Controllers;

import com.example.BarclaysTest.Service.UserService;
import com.example.BarclaysTest.model.Requests.CreateUserRequest;
import com.example.BarclaysTest.model.Requests.UpdateUserRequest;
import com.example.BarclaysTest.model.Response.UserResponse;
import com.example.BarclaysTest.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.BarclaysTest.util.JwtUtil.getAuthenticatedUserId;

@RestController
@RequestMapping("/v1/users")
@Tag(name = "user")
@Validated
public class UserEndpoint {

    @Autowired
    private UserService userService;


    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest userRequest){
        //Handle exception for is user is already created
         User user = userService.createUser(userRequest);
         UserResponse response = new UserResponse(user);
         return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/{userId}")
    @Operation(summary ="Fetch user by ID")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserResponse> fetchUserByID(
            @PathVariable  @Pattern(regexp = "usr-[A-Za-z0-9]+") String userId) {
        String authenticatedUserId = getAuthenticatedUserId();
        User user = userService.getUserIfAuthorized(userId,authenticatedUserId);
        UserResponse userResponse = new UserResponse(user);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @PatchMapping("/{userId}")
    @Operation(summary ="Update user by ID.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserResponse> updateUserByID(
            @PathVariable @Pattern(regexp = "usr-[A-Za-z0-9]+") String userId, @RequestBody UpdateUserRequest request) {
        String authenticatedUserId = getAuthenticatedUserId();
        User user = userService.updateUserDetails(userId,authenticatedUserId,request);
        UserResponse userResponse = new UserResponse(user);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary ="Delete user by ID")
    @SecurityRequirement(name = "bearerAuth")
    public void deleteUserByID(
            @PathVariable @Pattern(regexp = "usr-[A-Za-z0-9]+") String userId){
        String authenticatedUserId = getAuthenticatedUserId();
        userService.deleteUser(userId,authenticatedUserId);
    }



}
