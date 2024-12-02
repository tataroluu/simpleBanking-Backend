package com.eteration.simplebanking.services;

import com.eteration.simplebanking.dto.AccountRequestDto;
import com.eteration.simplebanking.dto.TransactionRequestDto;
import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.Transaction;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.constraints.NotBlank;

public interface AccountService {

    Account createAccount(AccountRequestDto accountRequestDto);

    void credit(String accountNumber, Transaction transaction);

    void debit(@NotBlank String accountNumber, Transaction transaction) throws AccountNotFoundException;

    Account findAccount(String accountNumber);

    Account processTransaction(String accountId, TransactionRequestDto transactionRequest);

 }