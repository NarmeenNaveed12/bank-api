package com.example.BarclaysTest.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Balance must be at least 0.00")
    @DecimalMax(value = "10000.00", inclusive = true, message = "Balance must not more than 10000.00")
    public double balance;
    public Currency currency;
    public LocalDateTime createdTimestamp;
    public LocalDateTime updatedTimestamp;
    @Pattern(regexp = "usr-[A-Za-z0-9]+", message = "unique id")
    public String userId;
}
