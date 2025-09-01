package com.example.BarclaysTest.model.Response;

import com.example.BarclaysTest.model.AccountType;
import com.example.BarclaysTest.model.BankAccount;
import com.example.BarclaysTest.model.Currency;
import com.example.BarclaysTest.model.SortCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BankAccountResponse {
    public String accountNumber;
    public SortCode sortCode;
    public String name;
    public AccountType accountType;
    public double balance;
    public Currency currency;
    public LocalDateTime createdTimestamp;
    public LocalDateTime updatedTimestamp;

    public BankAccountResponse(BankAccount bankAccount){
        this.accountNumber = bankAccount.getAccountNumber();
        this.name = bankAccount.getName();
        this.sortCode = bankAccount.getSortCode();
        this.accountType = bankAccount.getAccountType();
        this.balance = bankAccount.getBalance();
        this.currency = bankAccount.getCurrency();
        this.createdTimestamp = bankAccount.getCreatedTimestamp();
        this.updatedTimestamp = bankAccount.getUpdatedTimestamp();

    }
}
