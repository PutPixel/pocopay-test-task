package com.github.pocopay.service;

import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.pocopay.AbstractTest;
import com.github.pocopay.model.Account;

@Transactional
public class AccountServiceTestCase extends AbstractTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void get_accounts() {
        Collection<Account> accounts = accountService.findAll();

        Assert.assertNotNull(accounts);
        Assert.assertEquals(2, accounts.size());
    }

    @Test
    public void get_account() {
        Long id = new Long(1);

        Account account = accountService.find(id);

        Assert.assertNotNull(account);
        Assert.assertEquals(id, account.getId());
    }

    @Test
    public void get_account_not_found() {
        Long id = Long.MAX_VALUE;
        Account account = accountService.find(id);
        Assert.assertNull(account);
    }

    @Test
    public void create_account() {
        Account account = new Account();
        String name = "Test 3";
        BigDecimal amount = new BigDecimal("6.00");

        account.setName(name);
        account.setAmount(amount);

        Account createdAccount = accountService.create(account);

        Assert.assertNotNull(createdAccount);
        Assert.assertNotNull(createdAccount.getId());
        Assert.assertEquals(name, createdAccount.getName());
        Assert.assertEquals(amount, createdAccount.getAmount());

        Collection<Account> accounts = accountService.findAll();

        Assert.assertEquals(3, accounts.size());
    }

    @Test(expected = EntityExistsException.class)
    public void create_account_with_id() {
        Account account = new Account();
        account.setId(Long.MAX_VALUE);
        accountService.create(account);
    }

    @Test
    public void tarnsfer_money_success() {
        Long fromId = new Long(1);
        Long toId = new Long(2);
        BigDecimal amount = new BigDecimal("4.00");

        Account from = accountService.find(fromId);
        Account to = accountService.find(toId);
        accountService.transfer(from, to, amount);

        Account fromAfter = accountService.find(fromId);
        Assert.assertEquals(fromAfter.getAmount(), new BigDecimal("6.00"));

        Account toAfter = accountService.find(toId);
        Assert.assertEquals(toAfter.getAmount(), new BigDecimal("6.00"));
    }

    @Test(expected = NoResultException.class)
    public void tarnsfer_money_null_amount() {
        Long fromId = new Long(1);
        Long toId = new Long(2);

        Account from = accountService.find(fromId);
        Account to = accountService.find(toId);
        accountService.transfer(from, to, null);
    }

    @Test(expected = NoResultException.class)
    public void tarnsfer_money_more_then_possible() {
        Long fromId = new Long(1);
        Long toId = new Long(2);
        BigDecimal amount = new BigDecimal("14.00");

        Account from = accountService.find(fromId);
        Account to = accountService.find(toId);
        accountService.transfer(from, to, amount);
    }
}
