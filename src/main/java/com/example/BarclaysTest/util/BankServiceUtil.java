package com.example.BarclaysTest.util;

import com.example.BarclaysTest.ExceptionHandler.ForbiddenException;
import com.example.BarclaysTest.ExceptionHandler.NotFoundException;
import com.example.BarclaysTest.model.*;
import com.example.BarclaysTest.model.Requests.CreateBankAccountRequest;
import com.example.BarclaysTest.model.Requests.CreateTransactionRequest;
import com.example.BarclaysTest.model.Requests.CreateUserRequest;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public  class BankServiceUtil {
    public static String generateAccountId() {
        Random random = new Random();
        int sixDigits = random.nextInt(1_000_000);
        return String.format("01%06d", sixDigits);
    }

    public static String generateTransactionId() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String randomPart = uuid.substring(0, 6);
        return "tan-" + randomPart;
    }
    // format: ^usr-[A-Za-z0-9]+$
    public static String generateUserId(){
        return "usr-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static void validateBankAccountAndUser(BankAccount bankAccount, String userId, String errorMessage) {
        if (bankAccount == null) {
            throw new NotFoundException("Bank account not found");
        }
        if (!bankAccount.getUserId().equals(userId)) {
            throw new ForbiddenException(errorMessage);
        }
    }

    public static Transaction buildTransaction(CreateTransactionRequest request, String userId, TransactionType type) {
        // Build the transaction
        Transaction transaction = Transaction.builder()
                .type(type)
                .id(generateTransactionId())
                .amount(request.getAmount())
                .currency(Currency.GBP)
                .createdTimestamp(LocalDateTime.now())
                .userId(userId)
                .build();
        return transaction;
    }

    public static User buildUser(CreateUserRequest userRequest){
        User user = User.builder()
                .id(generateUserId())
                .name(userRequest.getName())
                .address(userRequest.getAddress())
                .phoneNumber(userRequest.getPhoneNumber())
                .email(userRequest.getEmail()).build();
        return user;

    }

    public static BankAccount buildBankAccount(CreateBankAccountRequest request, String userId, String accountID){
        BankAccount bankAccount = BankAccount.builder()
                .userId(userId)
                .accountNumber(accountID)
                .name(request.getName())
                .accountType(request.getAccountType())
                .balance(0.0)
                .sortCode(SortCode.TEN_TEN_TEN)
                .name(request.getName())
                .currency(Currency.GBP)
                .createdTimestamp(LocalDateTime.now())
                .updatedTimestamp(LocalDateTime.now()).build();
        return bankAccount;

    }
}
