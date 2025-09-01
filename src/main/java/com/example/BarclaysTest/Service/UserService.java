package com.example.BarclaysTest.Service;

import com.example.BarclaysTest.ExceptionHandler.ConflictException;
import com.example.BarclaysTest.ExceptionHandler.ForbiddenException;
import com.example.BarclaysTest.ExceptionHandler.NotFoundException;
import com.example.BarclaysTest.model.Requests.CreateUserRequest;
import com.example.BarclaysTest.model.Requests.UpdateUserRequest;
import com.example.BarclaysTest.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.BarclaysTest.util.BankServiceUtil.buildUser;

@Service
public class UserService {

    Map<String, User> usersCreated = new HashMap<>(); //as we dont have a db or else we would have saved there

    public boolean isUserCreated(String userId){
        return usersCreated.containsKey(userId);
    }

    public User fetchUserFromMap(String userId){
       return usersCreated.getOrDefault(userId, null);
    }

    public User createUser(CreateUserRequest userRequest){
        User user = buildUser(userRequest);
        usersCreated.put(user.getId(),user);
        return user;

    }
    public User getUserIfAuthorized(String userId, String authenticatedUserId){
            if (!isUserCreated(userId)) {
                throw new NotFoundException("User was not found");
            }
            User user = fetchUserFromMap(userId);
            if (!isCorrectUser(userId, authenticatedUserId)) {
                throw new ForbiddenException("The user is not allowed to access the transaction");
            }
            return user;
    }

    public boolean isCorrectUser(String userId, String authenticatedUserId){
        return Objects.equals(authenticatedUserId, userId);
    }

    public User updateUserDetails(String userId, String authUserId, UpdateUserRequest request){
        User user = getUserIfAuthorized(userId, authUserId);
        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        return user;
    }

    public ResponseEntity<Void> deleteUser(String userId, String authUserId){
        User user = getUserIfAuthorized(userId, authUserId);
        if(!user.getBankAccounts().isEmpty()){
            throw new ConflictException("A user cannot be deleted when they are associated with a bank account");
        }
        usersCreated.remove(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
