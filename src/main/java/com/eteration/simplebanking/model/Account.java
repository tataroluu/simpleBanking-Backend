package com.eteration.simplebanking.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Data
@Entity
@NoArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String owner;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private double balance;

    @OneToMany(fetch = EAGER, mappedBy = "account")
    private Set<Transaction> transactions;

    public Account(String owner, String accountNumber) {
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.balance = 0.0;
    }

    public void credit(double amount) {
        validateAmount(amount);
        this.balance += amount;
    }

    public void debit(double amount) throws InsufficientBalanceException {
        validateAmount(amount);
        if (amount > this.balance) {
            throw new InsufficientBalanceException("Insufficient balance for this transaction.");
        }
        this.balance -= amount;
    }

    public void deposit(double amount) {
        validateAmount(amount);
        this.balance += amount;
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        validateAmount(amount);
        if (this.balance < amount) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal.");
        }
        this.balance -= amount;
    }

    public void post(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null.");
        }
        transaction.setAccount(this);
        transaction.apply(this);
        transactions.add(transaction);
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero.");
        }
    }
}