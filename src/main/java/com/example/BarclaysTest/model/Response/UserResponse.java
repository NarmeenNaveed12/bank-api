package com.example.BarclaysTest.model.Response;

import com.example.BarclaysTest.model.Address;
import com.example.BarclaysTest.model.User;
import lombok.Getter;

@Getter
public class UserResponse {

    public String id;
    public String name;
    public Address address;
    public String phoneNumber;
    public String email;

    public UserResponse(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.address = user.getAddress();
        this.phoneNumber = user.getPhoneNumber();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();

    }
}
