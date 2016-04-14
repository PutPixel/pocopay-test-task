package com.github.pocopay.web.api;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.github.pocopay.AbstractControllerTest;
import com.github.pocopay.model.Account;
import com.github.pocopay.service.AccountService;

@Transactional
public class AccountControllerTestCase extends AbstractControllerTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void get_accounts() throws Exception {
        String uri = "/api/accounts";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals(200, status);
        Assert.assertTrue(content.trim().length() > 0);

    }

    @Test
    public void get_account() throws Exception {
        String uri = "/api/accounts/{id}";
        Long id = new Long(1);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals(200, status);
        Assert.assertTrue(content.trim().length() > 0);
    }

    @Test
    public void get_account_not_found() throws Exception {
        String uri = "/api/accounts/{id}";
        Long id = Long.MAX_VALUE;

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals(404, status);
        Assert.assertTrue(content.trim().length() == 0);
    }

    @Test
    public void create_account() throws Exception {
        String uri = "/api/accounts";
        Account account = new Account();
        account.setName("test 4");
        account.setAmount(new BigDecimal(7));
        String inputJson = super.mapToJson(account);

        MvcResult result =
                mvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(inputJson)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals(201, status);
        Assert.assertTrue(content.trim().length() > 0);

        Account created = super.mapFromJson(content, Account.class);

        Assert.assertNotNull(created);
        Assert.assertNotNull(created.getId());
        Assert.assertEquals("test 4", created.getName());
        Assert.assertEquals(new BigDecimal("7.00"), created.getAmount());

    }

    @Test
    public void tarnsfer_money_success() throws Exception {
        String uri = "/api/accounts/transfer/{fromId}/{toId}/{amount}";
        Long fromId = new Long(1);
        Long toId = new Long(2);
        BigDecimal amount = new BigDecimal(4);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, fromId, toId, amount).accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = result.getResponse().getStatus();

        Assert.assertEquals(200, status);

        Account fromAfter = accountService.find(fromId);
        Assert.assertEquals(fromAfter.getAmount(), new BigDecimal("6.00"));

        Account toAfter = accountService.find(toId);
        Assert.assertEquals(toAfter.getAmount(), new BigDecimal("6.00"));
    }
}
