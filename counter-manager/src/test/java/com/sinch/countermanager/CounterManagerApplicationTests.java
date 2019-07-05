package com.sinch.countermanager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CounterManagerApplication.class)
@Profile("test")
public class CounterManagerApplicationTests {
	@Test
	public void contextLoads() throws InterruptedException {

	}

}
