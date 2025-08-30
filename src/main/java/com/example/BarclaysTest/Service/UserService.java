package com.example.BarclaysTest.Service;

import com.example.BarclaysTest.model.Requests.CreateUserRequest;
import com.example.BarclaysTest.model.Response.UserResponse;
import com.example.BarclaysTest.model.User;
import com.example.BarclaysTest.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private JwtUtil jwtUtil;

    //check for existing user (not part of req)
    Map<String, User> usersCreated = new HashMap<>();//as we dont have a db or elase we would have saved there

    //check if user is created
    public boolean isUserCreated(String userId){
        return usersCreated.containsKey(userId);
    }

    public User fetchUserFromMap(String userId){
       return usersCreated.getOrDefault(userId, null);
    }

    public User createUser(CreateUserRequest userRequest, String userId){
        User user = User.builder()
                        .id(userId)
                        .name(userRequest.getName())
                        .address(userRequest.getAddress())
                        .phoneNumber(userRequest.getPhoneNumber())
                        .email(userRequest.getEmail()).build();

        usersCreated.put(userId,user);
        return user;

    }
    public User getUserIfAuthorized(String userId, String authenticatedUserId){
            if (!isUserCreated(userId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found");
            }

            User user = fetchUserFromMap(userId);
            if (!isUserAuthorized(userId, authenticatedUserId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The user is not allowed to access the transaction");
            }
            return user;
    }

    public boolean isUserAuthorized(String userId, String authenticatedUserId){
        return Objects.equals(authenticatedUserId, userId);
    }
    // format: ^usr-[A-Za-z0-9]+$
    public String generateUserId(){
        return "usr-" + UUID.randomUUID().toString().substring(0, 8);
//        String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        String prefix = "user-";
//        Random random = new Random();
//        StringBuilder stringBuilder = new StringBuilder();
//        //will keep the length small 5
//        for(int i = 0; i<5; i++){
//            random.
//        }
    }

}
