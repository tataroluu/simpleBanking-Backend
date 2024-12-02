package com.eteration.simplebanking.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TransactionRequestDto {

    @NotNull(message = "Amount cannot be null")
    private Double amount;

    @NotNull(message = "Type cannot be null")
    private String type;

}