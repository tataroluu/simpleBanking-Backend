package com.eteration.simplebanking.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("DEPOSIT")
@NoArgsConstructor
public class DepositTransaction extends Transaction {

    @Column(name = "type", insertable = false, updatable = false)
    private String type;

    public DepositTransaction(double amount) {
        super(amount);
    }

    public DepositTransaction(double amount, String type) {
        super(amount);
        this.type = type;
    }

    @Override
    public void apply(Account account) {
        account.setBalance(account.getBalance() + this.getAmount());
    }
}