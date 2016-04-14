package com.github.pocopay;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.github.pocopay.service.AccountServiceTestCase;
import com.github.pocopay.web.api.AccountControllerTestCase;
import com.github.pocopay.web.api.AccountTxControllerTestCase;

@RunWith(Suite.class)
@SuiteClasses({
        AccountControllerTestCase.class,
        AccountTxControllerTestCase.class,
        AccountServiceTestCase.class,
})
public class TestSuite {

}
