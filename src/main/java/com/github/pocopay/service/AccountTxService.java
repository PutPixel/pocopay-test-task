package com.github.pocopay.service;

import java.util.List;

import com.github.pocopay.model.Account;
import com.github.pocopay.model.AccountTx;

public interface AccountTxService {

    List<AccountTx> findOutgoingTxs(Account sourceAccount);

}
