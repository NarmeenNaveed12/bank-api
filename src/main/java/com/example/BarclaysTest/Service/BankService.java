package com.example.BarclaysTest.Service;

import com.example.BarclaysTest.model.BankAccount;
import com.example.BarclaysTest.model.Currency;
import com.example.BarclaysTest.model.Requests.CreateBankAccountRequest;
import com.example.BarclaysTest.model.Response.BankAccountResponse;
import com.example.BarclaysTest.model.Response.UserResponse;
import com.example.BarclaysTest.model.SortCode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BankService {

    //user id and affiliated bank account 1 - many mapping o(1) lookup
    Map<String, List<BankAccount>> userBankAccounts = new HashMap<>();
    //accountid with affliated bank account 1-1 mapping o(1) lookup
    Map<String, BankAccount> bankAccountWithAcctId = new HashMap<>();

    public BankAccount createBankAccount(CreateBankAccountRequest request, String userId){
        String accountId = generateAccountId();
        BankAccount bankAccount = BankAccount.builder()
                .userId(userId)
                .accountNumber(accountId)
                .name(request.getName())
                .accountType(request.getAccountType())
                .balance(0.0)
                .sortCode(SortCode.TEN_TEN_TEN)
                .name(request.name)
                .currency(Currency.GBP)
                .createdTimestamp(LocalDateTime.now())
                .updatedTimestamp(LocalDateTime.now()).build();
        userBankAccounts.computeIfAbsent(userId, k -> new ArrayList<>()).add(bankAccount);
        bankAccountWithAcctId.computeIfAbsent(accountId, k -> bankAccount);
        return bankAccount;
    }

    public static String generateAccountId() {
        Random random = new Random();
        int sixDigits = random.nextInt(1_000_000); // 0 to 999999
        return String.format("01%06d", sixDigits);
    }

    public List<BankAccount> listBankAccounts(String userId){
        return userBankAccounts.getOrDefault(userId, Collections.emptyList());
    }

    public BankAccount fetchAccountByAccountNumber(String accountId){
        return bankAccountWithAcctId.getOrDefault(accountId, null);
    }
}
