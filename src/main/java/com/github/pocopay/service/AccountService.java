package com.github.pocopay.service;

import java.math.BigDecimal;
import java.util.Collection;

import com.github.pocopay.model.Account;

public interface AccountService {

    Collection<Account> findAll();

    Account find(Long id);

    Account create(Account account);

    void transfer(Account from, Account to, BigDecimal amount);
}
