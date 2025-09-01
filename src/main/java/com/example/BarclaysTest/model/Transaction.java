package com.example.BarclaysTest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class Transaction {
    private String id;
    private Currency currency;
    @JsonProperty("type")
    private TransactionType type;
    private double amount;
    private String userId;
    private LocalDateTime createdTimestamp;
    private String reference;

}
