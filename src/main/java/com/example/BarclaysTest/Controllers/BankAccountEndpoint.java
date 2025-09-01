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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.BarclaysTest.util.JwtUtil.getAuthenticatedUserId;

@RestController
@Tag(name = "account")
@RequestMapping("/v1/accounts")
@Validated
public class BankAccountEndpoint {

    @Autowired
    private BankService bankService;


    @PostMapping
    @Operation(summary="Create a new bank account")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BankAccountResponse> createAccount(@RequestBody @Valid CreateBankAccountRequest request){
        String authenticatedUserId = getAuthenticatedUserId();
         BankAccount bankAccount = bankService.createBankAccount(request,authenticatedUserId);
         BankAccountResponse bankAccountResponse = new BankAccountResponse(bankAccount);
         return ResponseEntity.status(HttpStatus.CREATED).body(bankAccountResponse);

    }

    @GetMapping
    @Operation(summary="List accounts")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<BankAccountResponse>> listBankAccounts(){

        String authenticatedUserId = getAuthenticatedUserId();
        List<BankAccount> bankAccounts = bankService.listBankAccounts(authenticatedUserId);
        List<BankAccountResponse> bankAccountResponse = bankAccounts.stream().map(bankAccount -> new BankAccountResponse(bankAccount)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(bankAccountResponse);

    }

    @GetMapping("/{accountNumber}")
    @Operation(summary="Fetch account by account number")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BankAccountResponse> fetchAccountByAccountNumber(
            @PathVariable
            @Pattern(regexp = "^01\\d{6}$") String accountNumber){

        String authenticatedUserId = getAuthenticatedUserId();
        BankAccount bankAccount = bankService.fetchAccountByAccountNumber(accountNumber,authenticatedUserId);
        BankAccountResponse bankAccountResponse = new BankAccountResponse(bankAccount);
        return ResponseEntity.status(HttpStatus.OK).body(bankAccountResponse); //200 RESPONSE

    }

    @PostMapping("/{accountNumber}/transactions")
    @Operation(summary="Create a new transaction")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TransactionResponse> createTransaction(
            @PathVariable
            @Pattern(regexp = "^01\\d{6}$") String accountNumber,
            @Valid
            @RequestBody CreateTransactionRequest request){

        String authenticatedUserId = getAuthenticatedUserId();
        Transaction transaction = bankService.createTransaction(accountNumber,request,authenticatedUserId);
        TransactionResponse transactionResponse = new TransactionResponse(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);

    }

    @GetMapping("/{accountNumber}/transactions/{transactionId}")
    @Operation(summary="Fetch transaction by ID")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TransactionResponse>fetchAccountTransactionByID(
            @PathVariable
            @Pattern(regexp = "^01\\d{6}$") String accountNumber,
            @PathVariable @Pattern(regexp = "^tan-[A-Za-z0-9]+$") String transactionId){

        String authenticatedUserId = getAuthenticatedUserId();
        Transaction transaction = bankService.fetchTransactionById(authenticatedUserId,accountNumber,transactionId);
        TransactionResponse transactionResponse = new TransactionResponse(transaction);
        return ResponseEntity.status(HttpStatus.OK).body(transactionResponse);
    }

    @PatchMapping("/{accountNumber}")
    @Operation(summary="Update account by account number")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BankAccountResponse> updateAccountByAccountNumber(
            @PathVariable
            @Pattern(regexp = "^01\\d{6}$") String accountNumber,
            @Valid
            @RequestBody UpdateBankAccountRequest request){
        String authenticatedUserId = getAuthenticatedUserId();
        BankAccount bankAccount = bankService.updateBankAccountDetails(accountNumber,authenticatedUserId,request);
        BankAccountResponse bankAccountResponse = new BankAccountResponse(bankAccount);
        return ResponseEntity.status(HttpStatus.OK).body(bankAccountResponse); //200 RESPONSE
    }

    @DeleteMapping("/{accountNumber}")
    @Operation(summary="Delete account by account number")
    @SecurityRequirement(name = "bearerAuth")
    public void deleteAccountByAccountNumber(
            @PathVariable
            @Pattern(regexp = "^01\\d{6}$") String accountNumber){
        String authenticatedUserId = getAuthenticatedUserId();
        bankService.deleteBankAccount(authenticatedUserId,accountNumber);
    }
}
