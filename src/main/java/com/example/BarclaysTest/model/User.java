package com.example.BarclaysTest.model;

import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class User {
    public String id;
    public String name;
    public Address address;
    public String phoneNumber;
    public String email;
//    @Builder.Default
    public List<BankAccount> bankAccounts = new ArrayList<>();
}
