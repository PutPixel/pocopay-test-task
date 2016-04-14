package com.github.pocopay.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;

import org.apache.ibatis.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pocopay.mappers.AccountMapper;
import com.github.pocopay.mappers.AccountTxMapper;
import com.github.pocopay.model.Account;
import com.github.pocopay.model.AccountTx;

@Service
public class AccountServiceBean implements AccountService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CounterService counterService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountTxMapper accountTxMapper;

    @Override
    public Collection<Account> findAll() {
        logger.info("> findAll");

        counterService.increment("method.invoked.AccountServiceBean.findAll");

        Collection<Account> accounts = accountMapper.findAll();

        logger.info("< findAll");
        return accounts;
    }

    @Override
    public Account find(Long id) {
        logger.info("> find {}", id);

        counterService.increment("method.invoked.AccountServiceBean.find");

        Account account = accountMapper.findAccountById(id);

        logger.info("< find {}", id);
        return account;
    }

    @Transactional
    @Override
    public Account create(Account account) {
        logger.info("> create");

        counterService.increment("method.invoked.AccountServiceBean.create");

        if (account.getId() != null) {
            logger.error("Attempted to create a Account, but id attribute was not null.");
            logger.info("< create");
            throw new EntityExistsException(
                    "Cannot create new Account with supplied id.  The id attribute must be null to create an entity.");
        }

        if (account.getName() == null) {
            logger.error("Attempted to create a Account, but name was null.");
            logger.info("< create");
            throw new PersistenceException(
                    "Cannot create new Account with empty name");
        }

        BigDecimal amount = account.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            logger.error("Attempted to create a Account, but amount was incorrect");
            logger.info("< create");
            throw new PersistenceException(
                    "Cannot create new Account with absent or negative amount");
        }

        accountMapper.save(account);

        logger.info("< create");
        return find(account.getId());
    }

    @Transactional
    @Override
    public void transfer(Account from, Account to, BigDecimal amount) {
        logger.info("> transfer from {}, to {}, amount {}", from.getId(), to.getId(), amount);
        counterService.increment("method.invoked.AccountServiceBean.transfer");

        if (amount == null) {
            logger.error(
                    "Attempted to transfer money, but no amount specified");
            logger.info("< transfer from {}, to {}, amount {}", from.getId(), to.getId(), amount);
            throw new NoResultException("No amount specified");
        }

        if (from.getAmount().compareTo(amount) < 0) {
            logger.error(
                    "Attempted to transfer money, but source account have not enough money");
            logger.info("< transfer from {}, to {}, amount {}", from.getId(), to.getId(), amount);
            throw new NoResultException("Not enough money on source account");
        }

        from.setAmount(from.getAmount().subtract(amount));
        to.setAmount(to.getAmount().add(amount));

        AccountTx tx = new AccountTx();
        tx.setFromAccountId(from.getId());
        tx.setToAccountId(to.getId());
        tx.setDate(new Date());
        tx.setAmount(amount);

        accountTxMapper.save(tx);
        accountMapper.update(from);
        accountMapper.update(to);

        logger.info("< transfer from {}, to {}, amount {}", from.getId(), to.getId(), amount);
    }

}
