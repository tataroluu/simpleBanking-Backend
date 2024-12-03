package com.eteration.simplebanking.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("CREDIT")
@NoArgsConstructor
public class CreditTransaction extends Transaction {

    @Column(name = "type", insertable = false, updatable = false)
    private String type;

    public CreditTransaction(double amount) {
        super(amount);
    }
    public CreditTransaction(double amount, String type) {
        super(amount);
        this.type = type;
    }

    @Override
    public void apply(Account account) {
        account.setBalance(account.getBalance() + this.getAmount());
    }
}