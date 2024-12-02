package com.eteration.simplebanking.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
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
            throw new IllegalArgumentException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - this.getAmount());
    }
}