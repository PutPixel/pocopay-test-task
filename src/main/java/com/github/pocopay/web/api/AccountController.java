package com.github.pocopay.web.api;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pocopay.model.Account;
import com.github.pocopay.service.AccountService;

@RestController
public class AccountController extends BaseController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/api/accounts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Account>> getAccounts() {
        logger.info("> getAccounts");

        Collection<Account> accounts = accountService.findAll();

        logger.info("< getAccounts");
        return new ResponseEntity<Collection<Account>>(accounts, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/accounts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        logger.info("> getAccount");

        Account account = accountService.find(id);
        if (account == null) {
            logger.info("< getAccount");
            return new ResponseEntity<Account>(HttpStatus.NOT_FOUND);
        }

        logger.info("< getAccount");
        return new ResponseEntity<Account>(account, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/accounts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        logger.info("> createAccount");

        Account saved = accountService.create(account);

        logger.info("< createAccount");
        return new ResponseEntity<Account>(saved, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/accounts/transfer/{fromId}/{toId}/{amount}", method = RequestMethod.GET)
    public ResponseEntity<Void> transfer(@PathVariable Long fromId, @PathVariable Long toId, @PathVariable BigDecimal amount) {
        logger.info("> transfer");

        Account fromAccount = accountService.find(fromId);
        if (fromAccount == null) {
            logger.info("< getAccount from not found");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        Account toAccount = accountService.find(toId);
        if (toAccount == null) {
            logger.info("< getAccount to not found");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        accountService.transfer(fromAccount, toAccount, amount);

        logger.info("< transfer");
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
