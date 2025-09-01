package com.example.BarclaysTest.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.BarclaysTest.model.TransactionType.DEPOSIT;

@Data
@Builder
public class BankAccount {

    private String accountNumber;
    private SortCode sortCode;
    private String name;
    private AccountType accountType;
    private double balance;
    private Currency currency;
    private LocalDateTime createdTimestamp;
    private LocalDateTime updatedTimestamp;
    private String userId;
    private final List<Transaction> transactions = new ArrayList<>();

    public void depositMoney(double amount, Transaction transaction){
        if(this.balance + amount > 10000.00 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your balance cannot be more than 1000");
        }
        this.balance += amount;
        transactions.add(transaction);
    }

    public void withDrawMoney(double amount, Transaction transaction){
        if(this.balance < amount){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Insufficient funds to process transaction");
        }
        this.balance -= amount;
        transactions.add(transaction);
    }
}
