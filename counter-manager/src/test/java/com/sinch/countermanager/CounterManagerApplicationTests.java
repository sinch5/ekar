package com.sinch.countermanager;

import com.sinch.countermanager.services.ConcurrencyService;
import com.sinch.countermanager.services.CounterManagerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CounterManagerApplication.class)
@ContextConfiguration(classes= CounterManagerConfigTest.class)
@Profile("test")
public class CounterManagerApplicationTests {


	@Test
	public void contextLoads() throws InterruptedException {


	}

}
