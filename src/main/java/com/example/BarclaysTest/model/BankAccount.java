package com.example.BarclaysTest.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.BarclaysTest.model.TransactionType.DEPOSIT;

@Getter
@Setter
@Builder
public class BankAccount {

    @Pattern(regexp = "^01\\d{6}$", message = "Account number must start with 01 and have 6 more digits")
    public String accountNumber;

    //define a string of enum
    public SortCode sortCode;

    public String name;
    public AccountType accountType;

    @NotNull
    public double balance;
    public Currency currency;
    public LocalDateTime createdTimestamp;
    public LocalDateTime updatedTimestamp;
    @Pattern(regexp = "usr-[A-Za-z0-9]+", message = "unique id")
    public String userId;
    private final List<Transaction> transactions = new ArrayList<>();

    public void depositMoney(double amount, Transaction transaction){
        if(this.balance + amount > 10000.00 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }
        this.balance += amount;
        transactions.add(transaction);
    }

    public void withDrawMoney(double amount, Transaction transaction){
        if(this.balance < amount){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Insufficient funds to process transaction");
        }
        if(this.balance - amount < 0.00 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }
        this.balance -= amount;
        transactions.add(transaction);
    }
}
