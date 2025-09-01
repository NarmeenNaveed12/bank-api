package com.example.BarclaysTest.Service;

import com.example.BarclaysTest.ExceptionHandler.NotFoundException;
import com.example.BarclaysTest.model.*;
import com.example.BarclaysTest.model.Requests.CreateBankAccountRequest;
import com.example.BarclaysTest.model.Requests.CreateTransactionRequest;
import com.example.BarclaysTest.model.Requests.UpdateBankAccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.BarclaysTest.model.TransactionType.*;
import static com.example.BarclaysTest.util.BankServiceUtil.*;

@Service
public class BankService {

    @Autowired
    UserService userService;

    private Map<String, BankAccount> bankAccountWithAcctId = new HashMap<>();

    public BankAccount createBankAccount(CreateBankAccountRequest request, String userId){
        String accountId = generateAccountId();
        BankAccount bankAccount = buildBankAccount(request,userId,accountId);
        User user = userService.fetchUserFromMap(userId);
        user.getBankAccounts().add(bankAccount);
        bankAccountWithAcctId.computeIfAbsent(accountId, k -> bankAccount);
        return bankAccount;
    }

    public List<BankAccount> listBankAccounts(String userId){
        User user = userService.fetchUserFromMap(userId);
        return user.getBankAccounts();
    }

    public BankAccount fetchAccountByAccountNumber(String accountId, String userId){
        BankAccount bankAccount = fetchAccountFromMap(accountId);
        validateBankAccountAndUser(bankAccount, userId, "The user is not allowed to access the bank account details");

        return bankAccount;
    }
    public BankAccount fetchAccountFromMap(String accountId){
        return bankAccountWithAcctId.getOrDefault(accountId, null);
    }

    public Transaction createTransaction(String accountId, CreateTransactionRequest request, String userId){
        BankAccount bankAccount = fetchAccountFromMap(accountId);
        validateBankAccountAndUser(bankAccount, userId, "The user is not allowed to create a transaction for this bank account details");
        Transaction transaction;
        switch(request.transactionType) {
                case DEPOSIT:
                    transaction = buildTransaction(request, userId, DEPOSIT);
                    bankAccount.depositMoney(request.amount,transaction);
                    return transaction;
                case WITHDRAWAL:
                    transaction = buildTransaction(request, userId, WITHDRAWAL);
                    bankAccount.withDrawMoney(request.amount,transaction);
                    return transaction;
                default:
                    break;
            }

        return null;
    }

    public Transaction fetchTransactionById(String userId, String accountId, String transactionId){
        BankAccount bankAccount = fetchAccountFromMap(accountId);
        validateBankAccountAndUser(bankAccount, userId, "The user is not allowed to access the bank account details");

        return bankAccount.getTransactions().stream()
                    .filter(a -> a.getId().equals(transactionId)).findFirst()
                    .orElseThrow(() -> new NotFoundException("Transaction id was not found"));

    }

    public BankAccount updateBankAccountDetails(String accountNumber, String userId, UpdateBankAccountRequest request){
        BankAccount bankAccount = fetchAccountFromMap(accountNumber);
        validateBankAccountAndUser(bankAccount, userId, "The user is not allowed to update the bank account details");

        bankAccount.setAccountNumber(request.getName());
        bankAccount.setAccountType(request.accountType);

        return bankAccount;
    }


    public ResponseEntity<Void> deleteBankAccount(String userId, String accountNumber){
        BankAccount bankAccount = fetchAccountFromMap(accountNumber);
        validateBankAccountAndUser(bankAccount, userId, "The user is not allowed to delete the bank account details");

        User user = userService.fetchUserFromMap(userId);
        user.getBankAccounts().removeIf(a -> a.getAccountNumber().equals(accountNumber));
        bankAccountWithAcctId.remove(accountNumber);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
