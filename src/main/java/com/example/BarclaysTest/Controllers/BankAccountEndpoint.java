package com.example.BarclaysTest.Controllers;

import com.example.BarclaysTest.Service.BankService;
import com.example.BarclaysTest.Service.UserService;
import com.example.BarclaysTest.model.BankAccount;
import com.example.BarclaysTest.model.ErrorModel.BadRequestErrorResponse;
import com.example.BarclaysTest.model.ErrorModel.ErrorResponse;
import com.example.BarclaysTest.model.Requests.CreateBankAccountRequest;
import com.example.BarclaysTest.model.Requests.CreateUserRequest;
import com.example.BarclaysTest.model.Response.BankAccountResponse;
import com.example.BarclaysTest.model.Response.UserResponse;
import com.example.BarclaysTest.util.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/accounts")  // base path for this controller
@Validated //to allow patern to work
public class BankAccountEndpoint {

    @Autowired
    private BankService bankService;


    @PostMapping
    @Description("Create a new bank account")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> create(@RequestBody @NotNull CreateBankAccountRequest request){
        ResponseEntity responseEntity = null;
        String authenticatedUserId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if(authenticatedUserId == null){

        }
        try {
            BankAccount bankAccount = bankService.createBankAccount(request,authenticatedUserId);
            BankAccountResponse bankAccountResponse = new BankAccountResponse(bankAccount);
            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(bankAccountResponse); //201 RESPONSE
            } catch (IllegalArgumentException e) {
                // 400 Bad Request
                BadRequestErrorResponse error = new BadRequestErrorResponse("Invalid details supplied");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            } catch (Exception e) {
                // 500
                ErrorResponse error = new ErrorResponse("An unexpected error occurred");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }


        return responseEntity;
    }

    @GetMapping
    @Description("List accounts")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> listBankAccounts(){
        ResponseEntity responseEntity = null;

        String authenticatedUserId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if(authenticatedUserId == null){

        }
        try {
            List<BankAccount> bankAccounts = bankService.listBankAccounts(authenticatedUserId);
            List<BankAccountResponse> bankAccountResponse = bankAccounts.stream().map(bankAccount -> new BankAccountResponse(bankAccount)).collect(Collectors.toList());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(bankAccountResponse); //200 RESPONSE
        } catch (Exception e) {
            // 500
            ErrorResponse error = new ErrorResponse("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }


        return responseEntity;
    }

    @GetMapping("/{accountId}")
    @Description("Fetch account by account number")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> fetchAccountByAccountNumber(
            @PathVariable
            @NotBlank
            @Pattern(regexp = "^01\\d{6}$") String accountId){

        ResponseEntity responseEntity = null;
        String authenticatedUserId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if(authenticatedUserId == null){
            ErrorResponse errorMsg = new ErrorResponse("The user was not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMsg);
        }
        try {
            BankAccount bankAccount = bankService.fetchAccountByAccountNumber(accountId);
            if(bankAccount == null){
                ErrorResponse errorMsg = new ErrorResponse("Bank account was not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
            }
            if(!Objects.equals(bankAccount.getUserId(), authenticatedUserId)){
                ErrorResponse errorMsg = new ErrorResponse("The user is not allowed to access the bank account details");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMsg);
            }
            BankAccountResponse bankAccountResponse = new BankAccountResponse(bankAccount);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(bankAccountResponse); //200 RESPONSE
        } catch (IllegalArgumentException e) {
            // 400 Bad Request
            BadRequestErrorResponse error = new BadRequestErrorResponse("The request didn't supply all the necessary data");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            // 500
            ErrorResponse error = new ErrorResponse("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }


        return responseEntity;
    }
}
