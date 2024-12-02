package com.eteration.simplebanking.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.eteration.simplebanking.controller.AccountController;
import com.eteration.simplebanking.controller.TransactionStatus;
import com.eteration.simplebanking.dto.TransactionRequestDto;
import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.InsufficientBalanceException;
import com.eteration.simplebanking.services.AccountService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTests {

    @Spy
    @InjectMocks
    private AccountController controller;

    @Mock
    private AccountService service;

    // Test 1: Credit operation
    @Test
    public void givenId_Credit_thenReturnJson() throws Exception {
        Account account = new Account("Kerem Karaca", "17892");
        doReturn(account).when(service).findAccount("17892");

        TransactionRequestDto transactionDto = new TransactionRequestDto();
        transactionDto.setAmount(1000.0);

        ResponseEntity<TransactionStatus> result = controller.credit("17892", transactionDto);

        verify(service, times(1)).findAccount("17892");
        assertEquals("OK", result.getBody().getStatus());
    }

    // Test 2: Credit and Debit operation
    @Test
    public void givenId_CreditAndThenDebit_thenReturnJson() throws Exception {
        Account account = new Account("Kerem Karaca", "17892");
        doReturn(account).when(service).findAccount("17892");

        TransactionRequestDto creditDto = new TransactionRequestDto();
        creditDto.setAmount(1000.0);

        ResponseEntity<TransactionStatus> result = controller.credit("17892", creditDto);

        TransactionRequestDto debitDto = new TransactionRequestDto();
        debitDto.setAmount(50.0);

        ResponseEntity<TransactionStatus> result2 = controller.debit("17892", debitDto);

        verify(service, times(2)).findAccount("17892");
        assertEquals("OK", result.getBody().getStatus());
        assertEquals("OK", result2.getBody().getStatus());
        assertEquals(950.0, account.getBalance(), 0.001);
    }

    // Test 3: Credit and then Debit more than balance
    @Test
    public void givenId_CreditAndThenDebitMoreGetException_thenReturnJson() throws Exception {
        Account account = new Account("Kerem Karaca", "17892");
        account.setBalance(1000.0);

        doReturn(account).when(service).findAccount("17892");

        // Credit operation
        TransactionRequestDto creditDto = new TransactionRequestDto();
        creditDto.setAmount(1000.0);

        ResponseEntity<TransactionStatus> result = controller.credit("17892", creditDto);
        assertEquals("OK", result.getBody().getStatus());

        // Debit operation with insufficient balance
        TransactionRequestDto debitDto = new TransactionRequestDto();
        debitDto.setAmount(5000.0);

        ResponseEntity<TransactionStatus> result2 = controller.debit("17892", debitDto);
        assertEquals("FAILED", result2.getBody().getStatus());

        // Verify that InsufficientBalanceException is thrown
        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            controller.debit("17892", debitDto);
        });
    }

    // Test 4: Get account details
    @Test
    public void givenId_GetAccount_thenReturnJson() throws Exception {
        Account account = new Account("Kerem Karaca", "17892");
        doReturn(account).when(service).findAccount("17892");

        ResponseEntity<Account> result = controller.getAccount("17892");

        verify(service, times(1)).findAccount("17892");
        assertEquals(account, result.getBody());
    }
}