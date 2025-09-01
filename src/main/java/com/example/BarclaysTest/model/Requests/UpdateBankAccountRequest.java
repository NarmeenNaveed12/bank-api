package com.example.BarclaysTest.model.Requests;

import com.example.BarclaysTest.model.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateBankAccountRequest {

    private String name;
    @NotNull
    private AccountType accountType;
}
