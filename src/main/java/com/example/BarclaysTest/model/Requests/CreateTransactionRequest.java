package com.example.BarclaysTest.model.Requests;

import com.example.BarclaysTest.model.Currency;
import com.example.BarclaysTest.model.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateTransactionRequest {
    @NotNull
    private Currency currency;
    @JsonProperty("type")
    @NotNull
    private TransactionType transactionType;
    @NotNull
    @DecimalMin(value = "0.00", inclusive = true, message = "amount must be at least 0.00")
    @DecimalMax(value = "10000.00", inclusive = true, message = "amount must not more than 10000.00")
    private Double amount;
    private String reference; //optional

}
