package com.example.BarclaysTest.Controllers;

import com.example.BarclaysTest.Service.BankService;
import com.example.BarclaysTest.model.BankAccount;
import com.example.BarclaysTest.model.Requests.CreateBankAccountRequest;
import com.example.BarclaysTest.model.Requests.CreateTransactionRequest;
import com.example.BarclaysTest.model.Requests.UpdateBankAccountRequest;
import com.example.BarclaysTest.model.Response.BankAccountResponse;
import com.example.BarclaysTest.model.Response.TransactionResponse;
import com.example.BarclaysTest.model.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.BarclaysTest.util.JwtUtil.getAuthenticatedUserId;

@RestController
@Tag(name = "account")
@RequestMapping("/v1/accounts")  // base path for this controller
@Validated //to allow patern to work
public class BankAccountEndpoint {

    @Autowired
    private BankService bankService;


    @PostMapping
    @Operation(summary="Create a new bank account")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> createAccount(@RequestBody @NotNull CreateBankAccountRequest request){
        String authenticatedUserId = getAuthenticatedUserId();
         BankAccount bankAccount = bankService.createBankAccount(request,authenticatedUserId);
         BankAccountResponse bankAccountResponse = new BankAccountResponse(bankAccount);
         return ResponseEntity.status(HttpStatus.CREATED).body(bankAccountResponse); //201 RESPONSE

    }

    @GetMapping
    @Operation(summary="List accounts")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> listBankAccounts(){

        String authenticatedUserId = getAuthenticatedUserId();
        List<BankAccount> bankAccounts = bankService.listBankAccounts(authenticatedUserId);
        List<BankAccountResponse> bankAccountResponse = bankAccounts.stream().map(bankAccount -> new BankAccountResponse(bankAccount)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(bankAccountResponse);

    }

    @GetMapping("/{accountId}")
    @Operation(summary="Fetch account by account number")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> fetchAccountByAccountNumber(
            @PathVariable
            @NotBlank
            @Pattern(regexp = "^01\\d{6}$") String accountId){

        String authenticatedUserId = getAuthenticatedUserId();
            BankAccount bankAccount = bankService.fetchAccountByAccountNumber(accountId);
            if(bankAccount == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank account was not found");
            }
            if(!Objects.equals(bankAccount.getUserId(), authenticatedUserId)){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The user is not allowed to access the bank account details");
            }
            BankAccountResponse bankAccountResponse = new BankAccountResponse(bankAccount);
            return ResponseEntity.status(HttpStatus.OK).body(bankAccountResponse); //200 RESPONSE

    }

    @PostMapping("/{accountId}/transactions")
    @Operation(summary="Create a new transaction")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> createTransaction(
            @PathVariable
            @Pattern(regexp = "^01\\d{6}$") String accountNumber,
            @RequestBody CreateTransactionRequest request){

        String authenticatedUserId = getAuthenticatedUserId();
        Transaction transaction = bankService.createTransaction(accountNumber,request,authenticatedUserId);
        TransactionResponse transactionResponse = new TransactionResponse(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse); //200 RESPONSE

    }

    @GetMapping("/{accountId}/transactions/{transactionId}")
    @Operation(summary="Fetch transaction by ID")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> fetchAccountTransactionByID(
            @PathVariable
            @Pattern(regexp = "^01\\d{6}$") String accountNumber,
            @PathVariable @Pattern(regexp = "^tan-[A-Za-z0-9]$") String transactionId){

        String authenticatedUserId = getAuthenticatedUserId();
        Transaction transaction = bankService.fetchTransactionById(authenticatedUserId,accountNumber,transactionId);
        TransactionResponse transactionResponse = new TransactionResponse(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse); //200 RESPONSE
    }

    @PatchMapping("/{accountId}")
    @Operation(summary="Update account by account number")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateAccountByAccountNumber(
            @PathVariable
            @Pattern(regexp = "^01\\d{6}$") String accountNumber,
            @RequestBody UpdateBankAccountRequest request){
        String authenticatedUserId = getAuthenticatedUserId();
        BankAccount bankAccount = bankService.updateBankAccountDetails(accountNumber,authenticatedUserId,request);
        BankAccountResponse bankAccountResponse = new BankAccountResponse(bankAccount);
        return ResponseEntity.status(HttpStatus.OK).body(bankAccountResponse); //200 RESPONSE
    }

    @DeleteMapping("/{accountId}")
    @Operation(summary="Delete account by account number")
    @SecurityRequirement(name = "bearerAuth")
    public void deleteAccountByAccountNumber(
            @PathVariable
            @Pattern(regexp = "^01\\d{6}$") String accountNumber){
        String authenticatedUserId = getAuthenticatedUserId();
        bankService.deleteBankAccount(authenticatedUserId,accountNumber);
    }
}
