package com.example.BarclaysTest.Service;

import com.example.BarclaysTest.ExceptionHandler.ConflictException;
import com.example.BarclaysTest.ExceptionHandler.NotFoundException;
import com.example.BarclaysTest.model.*;
import com.example.BarclaysTest.model.Currency;
import com.example.BarclaysTest.model.Requests.CreateBankAccountRequest;
import com.example.BarclaysTest.model.Requests.CreateTransactionRequest;
import com.example.BarclaysTest.model.Requests.UpdateBankAccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.BarclaysTest.model.TransactionType.*;

@Service
public class BankService {

    @Autowired
    UserService userService;
    //user id and affiliated bank account 1 - many mapping o(1) lookup
//    private Map<String, List<BankAccount>> userBankAccounts = new HashMap<>();
    //accountid with affliated bank account 1-1 mapping o(1) lookup
    private Map<String, BankAccount> bankAccountWithAcctId = new HashMap<>();

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

        User user = userService.fetchUserFromMap(userId);
//        if (user == null) {
//            throw new NotFoundException(userId);
//        }
        user.getBankAccounts().add(bankAccount);
        bankAccountWithAcctId.computeIfAbsent(accountId, k -> bankAccount);
        return bankAccount;
    }

    private String generateAccountId() {
        Random random = new Random();
        int sixDigits = random.nextInt(1_000_000); // 0 to 999999
        return String.format("01%06d", sixDigits);
    }

    private String generateTransactionId() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String randomPart = uuid.substring(0, 6);
        return "tan-" + randomPart;
    }

    public List<BankAccount> listBankAccounts(String userId){
        User user = userService.fetchUserFromMap(userId);
        return user.getBankAccounts();
    }

    public BankAccount fetchAccountByAccountNumber(String accountId){
        return bankAccountWithAcctId.getOrDefault(accountId, null);
    }

    public boolean validateAccount(BankAccount bankAccount){
        if (bankAccount == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank account was not found");
        }
        return true;
    }

    public Transaction createTransaction(String accountId, CreateTransactionRequest request, String userId){
        BankAccount bankAccount = fetchAccountByAccountNumber(accountId);
        validateAccount(bankAccount);
        if(!bankAccount.getUserId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The user is not allowed to create a transaction for this bank account details");
        }
        Transaction transaction;
        switch(request.transactionType) {
                case DEPOSIT:
                    transaction = createTransaction(bankAccount, request, userId, DEPOSIT);
                    bankAccount.depositMoney(request.amount,transaction);
                    return transaction;
                case WITHDRAWAL:
                    transaction = createTransaction(bankAccount, request, userId, DEPOSIT);
                    bankAccount.withDrawMoney(request.amount,transaction);
                    return transaction;
                default:
                    break;
            }

        return null;
    }

    public Transaction fetchTransactionById(String userId, String accountId, String transactionId){
        BankAccount bankAccount = fetchAccountByAccountNumber(accountId);

        validateAccount(bankAccount);
        if(!bankAccount.getUserId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The user is not allowed to access the bank account details");
        }

        return bankAccount.getTransactions().stream()
                    .filter(a -> a.id.equals(transactionId)).findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction id was not found"));

    }

    private Transaction createTransaction(BankAccount bankAccount, CreateTransactionRequest request, String userId, TransactionType type) {
        // Build the transaction
        Transaction transaction = Transaction.builder()
                .transactionType(type)
                .id(generateTransactionId()) // or generate inside service
                .amount(request.getAmount())
                .currency(Currency.GBP)
                .createdTimestamp(LocalDateTime.now())
                .userId(userId)
                .build();

        bankAccount.getTransactions().add(transaction);
        return transaction;
    }

    public BankAccount updateBankAccountDetails(String accountNumber, String userId, UpdateBankAccountRequest request){
        BankAccount bankAccount = fetchAccountByAccountNumber(accountNumber);
        validateAccount(bankAccount);
        if(!bankAccount.getUserId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The user is not allowed to update the bank account details");
        }
        bankAccount.setAccountNumber(request.getName());
        bankAccount.setAccountType(request.accountType);

        return bankAccount;
    }


    public ResponseEntity<Void> deleteBankAccount(String userId, String accountNumber){
        BankAccount bankAccount = fetchAccountByAccountNumber(accountNumber);
        validateAccount(bankAccount);
        if(!bankAccount.getUserId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The user is not allowed to delete the bank account details");
        }
        User user = userService.fetchUserFromMap(userId);
        user.getBankAccounts().removeIf(a -> a.getAccountNumber().equals(accountNumber));
        bankAccountWithAcctId.remove(accountNumber);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
