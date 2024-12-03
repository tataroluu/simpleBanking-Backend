package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.dto.AccountRequestDto;
import com.eteration.simplebanking.dto.TransactionRequestDto;
import com.eteration.simplebanking.dto.TransactionStatusDto;
import com.eteration.simplebanking.model.*;
import com.eteration.simplebanking.services.AccountService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/account/create")
    public ResponseEntity<Account> createAccount(@RequestBody @Valid AccountRequestDto accountRequestDto) {
        Account account = accountService.createAccount(accountRequestDto);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountId}/transactions")
    public ResponseEntity<TransactionStatusDto> processTransaction(
            @PathVariable String accountId,
            @RequestBody TransactionRequestDto transactionRequest) {
         Account account = accountService.processTransaction(accountId, transactionRequest);
        return ResponseEntity.ok(new TransactionStatusDto("OK", account.getBalance()));
    }



    @PostMapping("/account/credit/{accountNumber}")
    public ResponseEntity<TransactionStatusDto> credit(@PathVariable @NotBlank String accountNumber,
                                                    @RequestBody @Valid TransactionRequestDto transactionRequestDto) {
        Account account = accountService.findAccount(accountNumber);

        Transaction transaction;
        switch (transactionRequestDto.getType().toUpperCase()) {
            case "WITHDRAWAL":
                transaction = new WithdrawalTransaction(transactionRequestDto.getAmount());
                break;
            case "DEBIT":
                transaction = new DebitTransaction(transactionRequestDto.getAmount());
                break;
            case "DEPOSIT":
                transaction = new DepositTransaction(transactionRequestDto.getAmount());
                break;
            case "CREDIT":
                transaction = new CreditTransaction(transactionRequestDto.getAmount());
                break;
            default:
                return ResponseEntity.badRequest().body(new TransactionStatusDto("Invalid transaction type", account.getBalance()));
        }

        transaction.setAccount(account);
        accountService.credit(accountNumber, transaction);

        accountService.credit(accountNumber, transaction);
        return ResponseEntity.ok(new TransactionStatusDto("OK", account.getBalance()));
    }



    @PostMapping("/account/debit/{accountNumber}")
    public ResponseEntity<TransactionStatusDto> debit(@PathVariable @NotBlank String accountNumber,
                                                   @RequestBody @Valid TransactionRequestDto transactionRequestDto){

        Account account = accountService.findAccount(accountNumber);

        Transaction transaction = null;
        if ("WITHDRAWAL".equalsIgnoreCase(transactionRequestDto.getType())) {
            transaction = new WithdrawalTransaction(transactionRequestDto.getAmount());
        } else if ("DEBIT".equalsIgnoreCase(transactionRequestDto.getType())) {
            transaction = new DebitTransaction(transactionRequestDto.getAmount());
        } else if ("DEPOSIT".equalsIgnoreCase(transactionRequestDto.getType())) {
                transaction = new DepositTransaction(transactionRequestDto.getAmount());
        } else {
            return ResponseEntity.badRequest().body(new TransactionStatusDto("Invalid transaction type", account.getBalance()));
        }

        transaction.setAccount(account);

        try {
            accountService.debit(accountNumber, transaction);
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body(new TransactionStatusDto("Insufficient balance", account.getBalance()));
        }

        return ResponseEntity.ok(new TransactionStatusDto("OK", account.getBalance()));
    }



    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable @NotBlank String accountNumber) {
        Account account = accountService.findAccount(accountNumber);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(account);
    }


}