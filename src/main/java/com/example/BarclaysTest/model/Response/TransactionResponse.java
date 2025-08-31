package com.example.BarclaysTest.model.Response;

import com.example.BarclaysTest.model.BankAccount;
import com.example.BarclaysTest.model.Currency;
import com.example.BarclaysTest.model.Transaction;
import com.example.BarclaysTest.model.TransactionType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public class TransactionResponse {
    @Pattern(regexp = "^tan-[A-Za-z0-9]$", message = "unique id")
    private String id;
    @NotNull
    private Currency currency;
    @NotNull
    private TransactionType transactionType;
    @NotNull
    private Double amount;
    @NotNull
    private LocalDateTime createdTimestamp;
    private String reference; //optional
    private String userId; //optional

    public TransactionResponse(Transaction transaction){
        this.id = transaction.getId();
        this.currency = transaction.getCurrency();
        this.transactionType = transaction.getTransactionType();
        this.amount = transaction.getAmount();
        this.createdTimestamp = transaction.getCreatedTimestamp();
        this.reference = transaction.getReference();
        this.userId = transaction.getUserId();
    }
}
