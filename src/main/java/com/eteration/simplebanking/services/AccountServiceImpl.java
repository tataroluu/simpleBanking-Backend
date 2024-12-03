package com.eteration.simplebanking.services;

import com.eteration.simplebanking.dto.AccountRequestDto;
import com.eteration.simplebanking.dto.TransactionRequestDto;
import com.eteration.simplebanking.model.*;
import com.eteration.simplebanking.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    Logger logger =LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(AccountRequestDto accountRequestDto) {
        Account account = new Account(accountRequestDto.getOwner(), accountRequestDto.getAccountNumber());
        return accountRepository.save(account);
    }

    @Override
    public void credit(String accountNumber, Transaction transaction) {
        Account account = findAccount(accountNumber);
        transaction.apply(account);
        account.getTransactions().add(transaction);
        accountRepository.save(account);
    }

    public void debit(String accountNumber, Transaction transaction) {
        Account account = findAccount(accountNumber);

        if (account.getBalance() < transaction.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance to complete the transaction.");
        }
        transaction.apply(account);
        account.getTransactions().add(transaction);
        accountRepository.save(account);
    }


    @Override
    public Account findAccount(String accountNumber) {

        Optional<Account> accountOptional = accountRepository.findByAccountNumber(accountNumber);

        if (accountOptional.isPresent()) {
            return accountOptional.get();
        } else {
            logger.info("Account with number {} not found");
            return null;
        }
    }

    public Account processTransaction(String accountId, TransactionRequestDto transactionRequest) {
        Account account = accountRepository.findByAccountNumber(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Transaction transaction;
        switch (transactionRequest.getType().toUpperCase()) {
            case "DEPOSIT":
                transaction = new DepositTransaction(transactionRequest.getAmount());
                break;
            case "WITHDRAWAL":
                transaction = new WithdrawalTransaction(transactionRequest.getAmount());
                break;
            case "CREDIT":
                transaction = new CreditTransaction(transactionRequest.getAmount());
                break;
            default:
                throw new IllegalArgumentException("Unsupported transaction type: " + transactionRequest.getType());
        }

        account.post(transaction);
        accountRepository.save(account);
        return account;
    }
}

