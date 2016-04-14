package com.github.pocopay.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class Account extends TransactionalEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Account [name=" + name + ", amount=" + amount + "]";
    }

}
