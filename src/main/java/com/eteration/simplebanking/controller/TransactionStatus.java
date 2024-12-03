package com.eteration.simplebanking.controller;

import lombok.Data;

@Data
public class TransactionStatus {
    private String status;
    private double balance;

    public TransactionStatus(String status, double balance) {
        this.status = status;
        this.balance = balance;
    }
}