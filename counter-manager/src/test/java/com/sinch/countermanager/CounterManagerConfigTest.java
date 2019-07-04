package com.sinch.countermanager;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
@TestPropertySource(properties = "classpath:application-test.properties")
public class CounterManagerConfigTest {
/*
    @Bean
    public CounterManagerService getCounterManagerService() {
        return new CounterManagerServiceImpl();
    }*/

}
