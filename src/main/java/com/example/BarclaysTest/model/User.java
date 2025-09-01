package com.example.BarclaysTest.model;

import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class User {
    private String id;
    private String name;
    private Address address;
    private String phoneNumber;
    private String email;
    @Builder.Default
    private List<BankAccount> bankAccounts = new ArrayList<>();
}
