package com.example.BarclaysTest.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Transaction {

    @Pattern(regexp = "^tan-[A-Za-z0-9]$", message = "unique id")
    public String id;
    public Currency currency;
    public TransactionType transactionType;
    public double amount;
    @Pattern(regexp = "usr-[A-Za-z0-9]+", message = "userId must be in this format")
    public String userId;
    public LocalDateTime createdTimestamp;
    private String reference;


}
