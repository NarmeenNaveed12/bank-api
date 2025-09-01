package com.example.BarclaysTest.model.Response;
import com.example.BarclaysTest.model.Currency;
import com.example.BarclaysTest.model.Transaction;
import com.example.BarclaysTest.model.TransactionType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TransactionResponse {
    private String id;
    private Currency currency;
    private TransactionType type;
    private double amount;
    private LocalDateTime createdTimestamp;
    private String reference;
    private String userId;

    public TransactionResponse(Transaction transaction){
        this.id = transaction.getId();
        this.currency = transaction.getCurrency();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.createdTimestamp = transaction.getCreatedTimestamp();
        this.reference = transaction.getReference();
        this.userId = transaction.getUserId();
    }
}
