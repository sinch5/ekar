package com.sinch.countermanager.services.impl;

import com.sinch.countermanager.CounterManagerApplication;
import com.sinch.countermanager.services.ConcurrencyService;
import com.sinch.countermanager.services.CounterManagerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

/**
 * @author Sinchinov Yury
 *
 * Test of correctness concurrency
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CounterManagerApplication.class)
public class ConcurrencyServiceImplTests {

	@Autowired
	private CounterManagerService counterManagerService;

	@Autowired
	private ConcurrencyService concurrencyService;

	@Test
	public void start() throws InterruptedException {

		/*  if count of consumersis equal count of producers result must be equal to initial value   */
		CountDownLatch countDownLatch = new CountDownLatch(200);
		concurrencyService.start(100, 100, countDownLatch);
		countDownLatch.await();
		assert(counterManagerService.getValue().equals(50));
		/*----  if count of consumersis equal count of producers result must be equal to initial value   ----*/

		countDownLatch = new CountDownLatch(400);
		concurrencyService.start(200, 200, countDownLatch);
		countDownLatch.await();
		assert(counterManagerService.getValue().equals(50));

		countDownLatch  = new CountDownLatch(800);
		concurrencyService.start(400, 400, countDownLatch); // producers> customers on 50. Prodecers will reach 100 and all producer thread will be in wait status
		//counterManagerService.setValue(40); //Set Counter value to 40. Producers should wake up and add rest 50 iterations
		countDownLatch.await();
		assert(counterManagerService.getValue().equals(50)); //Result must be 90

		countDownLatch  = new CountDownLatch(150);
		concurrencyService.start(100, 50, countDownLatch); // producers> customers on 50. Prodecers will reach 100 and all producer thread will be in wait status
		//counterManagerService.setValue(40); //Set Counter value to 40. Producers should wake up and add rest 50 iterations
		countDownLatch.await();
		assert(counterManagerService.getValue().equals(100)); //Result must be 90

	}
}
