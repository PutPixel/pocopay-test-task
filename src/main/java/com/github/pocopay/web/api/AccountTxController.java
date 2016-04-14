package com.github.pocopay.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pocopay.model.Account;
import com.github.pocopay.model.AccountTx;
import com.github.pocopay.service.AccountService;
import com.github.pocopay.service.AccountTxService;

@RestController
public class AccountTxController extends BaseController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountTxService accountTxService;

    @RequestMapping(value = "/api/txs/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AccountTx>> getTxs(@PathVariable Long id) {
        logger.info("> getTxs");

        Account account = accountService.find(id);
        if (account == null) {
            logger.info("< getTxs");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<AccountTx> txs = accountTxService.findOutgoingTxs(account);

        logger.info("< getTxs");
        return new ResponseEntity<>(txs, HttpStatus.OK);
    }
}
