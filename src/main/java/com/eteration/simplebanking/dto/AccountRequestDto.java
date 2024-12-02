package com.eteration.simplebanking.dto;



import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AccountRequestDto {

    @NotBlank(message = "Owner name is required")
    private String owner;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    private double initialBalance;
}
