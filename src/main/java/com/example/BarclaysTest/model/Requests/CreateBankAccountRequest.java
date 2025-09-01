package com.example.BarclaysTest.model.Requests;

import com.example.BarclaysTest.model.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor //for testing
public class CreateBankAccountRequest {
    @NotBlank
    public String name;
    @NotNull
    public AccountType accountType;
}
