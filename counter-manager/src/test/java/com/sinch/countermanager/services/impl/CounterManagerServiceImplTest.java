package com.sinch.countermanager.services.impl;

import com.sinch.countermanager.CounterManagerApplication;
import com.sinch.countermanager.services.CounterManagerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sinchinov Yury
 * Testing invariants
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CounterManagerApplication.class)
public class CounterManagerServiceImplTest {

    @Autowired
    private CounterManagerService counterManagerService;

    @Test(expected = RuntimeException.class)
    public void setInitialValueMoreThanHigh() {
        counterManagerService.setValue(300);
    }

    @Test(expected = RuntimeException.class)
    public void setInitialValueLessThanLow() {
        counterManagerService.setValue(-10);
    }

    @Test
    public void setInitialValue() {
        counterManagerService.setValue(60);
        assert(counterManagerService.getValue().equals(60));
    }
}