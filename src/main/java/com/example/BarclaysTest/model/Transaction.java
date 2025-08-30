package com.example.BarclaysTest.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Transaction {

    @Pattern(regexp = "^tan-[A-Za-z0-9]$", message = "unique id")
    public String id;
    @NotNull(message = "Phone number is required")
    @Pattern(regexp = "^\\+\\d{10,15}$", message = "Phone number must be in the format +1234567890")
    public String phoneNumber;
    public Currency currency;
    public TransactionType transactionType;
    @DecimalMin(value = "0.00", inclusive = true, message = "amount must be at least 0.00")
    @DecimalMax(value = "10000.00", inclusive = true, message = "amount must not more than 10000.00")
    public double amount;
    @Pattern(regexp = "usr-[A-Za-z0-9]+", message = "userId must be in this format")
    public String userId;
    public LocalDateTime createdTimestamp;


}
