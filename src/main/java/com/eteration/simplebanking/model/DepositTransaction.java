package com.eteration.simplebanking.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("DEPOSIT")
@NoArgsConstructor
public class DepositTransaction extends Transaction {

    public DepositTransaction(double amount) {
        super(amount);
    }


    @Override
    public void apply(Account account) {
        account.setBalance(account.getBalance() + this.getAmount());
    }
}