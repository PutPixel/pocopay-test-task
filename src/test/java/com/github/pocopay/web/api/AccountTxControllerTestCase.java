package com.github.pocopay.web.api;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.pocopay.AbstractControllerTest;
import com.github.pocopay.model.AccountTx;

@Transactional
public class AccountTxControllerTestCase extends AbstractControllerTest {

    private static final BigDecimal TX_AMOUNT = new BigDecimal("2.00");

    @Test
    public void no_txs_made() throws Exception {
        String uri = "/api/txs/{id}";
        Long account = 1L;

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, account).accept(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals(200, status);
        Assert.assertEquals(content.trim(), "[]");
    }

    @Test
    public void transfer_money_from_first_account_single_tx_expected() throws Exception {
        transferMoney(1L, 2L);

        Long account = 1L;

        List<AccountTx> list = getTxsForAccount(account);

        assertEquals(1, list.size());

        AccountTx tx = list.get(0);
        assertEquals(1L, tx.getFromAccountId());
        assertEquals(2L, tx.getToAccountId());
        assertEquals(TX_AMOUNT, tx.getAmount());
    }

    @Test
    public void transfer_money_from_first_account_twice_no_tx_expected_on_second_account_and_two_on_first() throws Exception {
        transferMoney(1L, 2L);
        transferMoney(1L, 2L);

        List<AccountTx> list1 = getTxsForAccount(2L);
        assertEquals(0, list1.size());

        List<AccountTx> list2 = getTxsForAccount(1L);

        assertEquals(2, list2.size());

        AccountTx tx1 = list2.get(0);
        assertEquals(1L, tx1.getFromAccountId());
        assertEquals(2L, tx1.getToAccountId());
        assertEquals(TX_AMOUNT, tx1.getAmount());

        AccountTx tx2 = list2.get(1);
        assertEquals(1L, tx2.getFromAccountId());
        assertEquals(2L, tx2.getToAccountId());
        assertEquals(TX_AMOUNT, tx2.getAmount());
    }

    private List<AccountTx> getTxsForAccount(Long account)
            throws Exception, UnsupportedEncodingException, JsonParseException, JsonMappingException, IOException {
        String uri = "/api/txs/{id}";
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, account).accept(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals(200, status);
        List<AccountTx> list = mapFromJson(content, new TypeReference<List<AccountTx>>() {
        });
        return list;
    }

    private void transferMoney(Long fromId, Long toId) throws Exception {
        String uri = "/api/accounts/transfer/{fromId}/{toId}/{amount}";
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, fromId, toId, TX_AMOUNT).accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = result.getResponse().getStatus();

        Assert.assertEquals(200, status);
    }

}
