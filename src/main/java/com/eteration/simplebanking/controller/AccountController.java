package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.dto.AccountRequestDto;
import com.eteration.simplebanking.dto.TransactionRequestDto;
import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.model.Transaction;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import com.eteration.simplebanking.services.AccountService;
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
    public ResponseEntity<TransactionStatus> processTransaction(
            @PathVariable String accountId,
            @RequestBody TransactionRequestDto transactionRequest) {
        accountService.processTransaction(accountId, transactionRequest);
        Account account = accountService.processTransaction(accountId, transactionRequest);
        return ResponseEntity.ok(new TransactionStatus("OK", account.getBalance()));
    }

    @PostMapping("/account/credit/{accountNumber}")
    public ResponseEntity<TransactionStatus> credit(@PathVariable @NotBlank String accountNumber,
                                                    @RequestBody @Valid DepositTransaction depositTransaction) {
        Account account = accountService.findAccount(accountNumber);

        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setType(depositTransaction.getType());
        transactionRequestDto.setAmount(depositTransaction.getAmount());

        Transaction transaction = null;
        if ("WITHDRAWAL".equalsIgnoreCase(transactionRequestDto.getType())) {
            transaction = new WithdrawalTransaction(transactionRequestDto.getAmount());
        } else if ("DEPOSIT".equalsIgnoreCase(transactionRequestDto.getType())) {
            transaction = new DepositTransaction(transactionRequestDto.getAmount());
         } else {
            return ResponseEntity.badRequest().body(new TransactionStatus("Invalid transaction type", account.getBalance()));
        }
        accountService.credit(accountNumber, transaction);
        return ResponseEntity.ok(new TransactionStatus("OK", account.getBalance()));
    }



    @PostMapping("/account/debit/{accountNumber}")
    public ResponseEntity<TransactionStatus> debit(@PathVariable @NotBlank String accountNumber,
                                                   @RequestBody @Valid WithdrawalTransaction withdrawalTransaction){

        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setAmount(withdrawalTransaction.getAmount());
        transactionRequestDto.setType(withdrawalTransaction.getType());

        Account account = accountService.findAccount(accountNumber);

        Transaction transaction = null;
        if ("WITHDRAWAL".equalsIgnoreCase(transactionRequestDto.getType())) {
            transaction = new WithdrawalTransaction(transactionRequestDto.getAmount());
        } else if ("DEPOSIT".equalsIgnoreCase(transactionRequestDto.getType())) {
            transaction = new DepositTransaction(transactionRequestDto.getAmount());
        } else {
            return ResponseEntity.badRequest().body(new TransactionStatus("Invalid transaction type", account.getBalance()));
        }

        try {
            accountService.debit(accountNumber, transaction);
        } catch (AccountNotFoundException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(new TransactionStatus("OK", account.getBalance()));
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