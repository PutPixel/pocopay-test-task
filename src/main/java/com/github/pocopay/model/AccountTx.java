package com.github.pocopay.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

public class AccountTx extends TransactionalEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    private long fromAccountId;

    @NotNull
    private long toAccountId;

    @NotNull
    private Date date;

    @NotNull
    private BigDecimal amount;

    public long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
