package com.eteration.simplebanking.dto;

import lombok.Data;

@Data
public class TransactionStatusDto {
    private String status;
    private double balance;

    public TransactionStatusDto(String status, double balance) {
        this.status = status;
        this.balance = balance;
    }
}