package com.eteration.simplebanking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class TransactionRequestDto {

    @NotNull(message = "Amount cannot be null")
    @NotBlank
    private double amount;

    @NotNull(message = "Type cannot be null")
    private String type;


    public TransactionRequestDto(double amount, String type) {
        this.amount = amount;
        this.type = type;
    }

    public TransactionRequestDto(double amount) {
        this.amount = amount;
    }
}