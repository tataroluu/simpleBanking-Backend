package com.eteration.simplebanking.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("WITHDRAWAL")
@NoArgsConstructor
public class WithdrawalTransaction extends Transaction {

    public WithdrawalTransaction(double amount) {
        super(amount);
    }

    @Override
    public void apply(Account account) {
        if (account.getBalance() < this.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance transaction.");
        }
        account.setBalance(account.getBalance() - this.getAmount());
    }
}