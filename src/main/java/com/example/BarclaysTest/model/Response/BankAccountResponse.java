package com.example.BarclaysTest.model.Response;

import com.example.BarclaysTest.model.AccountType;
import com.example.BarclaysTest.model.BankAccount;
import com.example.BarclaysTest.model.Currency;
import com.example.BarclaysTest.model.SortCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BankAccountResponse {
    @NotBlank
    @Pattern(regexp = "^01\\d{6}$", message = "Account number must start with 01 and have 6 more digits")
    public String accountNumber;

    //define a string of enum
    @NotNull
    //validation cascades. to nested level
    @Valid
    public SortCode sortCode;
    @NotBlank
    public String name;
    @NotNull
    @Valid
    public AccountType accountType;

    //the other way is to do this in a setter
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Balance must be at least 0.00")
    @DecimalMax(value = "10000.00", inclusive = true, message = "Balance must not more than 10000.00")
    public double balance;
    @NotNull
    @Valid
    public Currency currency;
    @NotNull
    public LocalDateTime createdTimestamp;
    @NotNull
    public LocalDateTime updatedTimestamp;

    public BankAccountResponse(BankAccount bankAccount){
        this.accountNumber = bankAccount.getAccountNumber();
        this.sortCode = bankAccount.getSortCode();
        this.accountType = bankAccount.getAccountType();
        this.balance = bankAccount.getBalance();
        this.createdTimestamp = bankAccount.getCreatedTimestamp();
        this.updatedTimestamp = bankAccount.getUpdatedTimestamp();

    }
}
