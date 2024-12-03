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
@DiscriminatorValue("WITHDRAWAL")
@NoArgsConstructor
public class WithdrawalTransaction extends Transaction {

    @Column(name = "type", insertable = false, updatable = false)
    private String type;

    public WithdrawalTransaction(double amount) {
        super(amount);
    }

    public WithdrawalTransaction(double amount, String type) {
        super(amount);
        this.type = type;
    }

    @Override
    public void apply(Account account) {
        if (account.getBalance() < this.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance transaction.");
        }
        account.setBalance(account.getBalance() - this.getAmount());
    }
}