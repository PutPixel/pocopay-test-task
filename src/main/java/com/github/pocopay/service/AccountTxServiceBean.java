package com.github.pocopay.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;

import com.github.pocopay.mappers.AccountTxMapper;
import com.github.pocopay.model.Account;
import com.github.pocopay.model.AccountTx;

@Service
public class AccountTxServiceBean implements AccountTxService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CounterService counterService;

    @Autowired
    private AccountTxMapper accountTxMapper;

    @Override
    public List<AccountTx> findOutgoingTxs(Account sourceAccount) {
        logger.info("> findOutgoingTxs {}", sourceAccount.getId());

        counterService.increment("method.invoked.AccountTxServiceBean.findOutgoingTxs");

        List<AccountTx> txs = accountTxMapper.findOutgoingTxs(sourceAccount.getId());

        logger.info("< findOutgoingTxs {}", sourceAccount.getId());
        return txs;
    }
}
