package com.example.BarclaysTest.model.Requests;

import com.example.BarclaysTest.model.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateBankAccountRequest {
    @NotBlank
    private String name;
    @NotNull
    private AccountType accountType;
}
