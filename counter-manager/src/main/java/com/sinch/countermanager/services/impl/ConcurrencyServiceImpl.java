package com.sinch.countermanager.services.impl;

import com.sinch.countermanager.model.BorderInfoEntity;
import com.sinch.countermanager.services.BorderInfoService;
import com.sinch.countermanager.services.ConcurrencyService;
import com.sinch.countermanager.services.CounterManagerService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @author Sinchinov Yury
 * This service start producers and consumers to change concurrently counter value
 */
@Service("concurrencyService")
public class ConcurrencyServiceImpl implements ConcurrencyService {

    public static final int HIGH_BORER = 100;
    public static final int LOW_BORER = 0;

    private final CounterManagerService counterManagerService;
    private final BorderInfoService borderInfoService;
    private final ExecutorService cachedThreadPool;

    private final ReentrantLock producersLock =  new ReentrantLock(true);

    private final Condition producedMsg  = producersLock.newCondition();
    private final Condition consumedMsg = producersLock.newCondition();

    private boolean borderReached;

    /**
     *
     * @param counterManagerService manager of Counter
     * @param cachedThreadPool pool
     * @param borderInfoService border reaching fixator
     */
    public ConcurrencyServiceImpl(CounterManagerService counterManagerService, ExecutorService cachedThreadPool, BorderInfoService borderInfoService) {
        this.counterManagerService = counterManagerService;
        this.cachedThreadPool = cachedThreadPool;
        this.borderInfoService = borderInfoService;
    }

    @Override
    public void start(int producers, int consumers) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(producers + consumers);

        startProducers(cyclicBarrier, Optional.empty(), producers);
        startConsumers(cyclicBarrier, Optional.empty(), consumers);
    }

    @Override
    public void start(int produces, int consumers, CountDownLatch countDownLatch) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(produces + consumers);

        startProducers(cyclicBarrier, Optional.of(countDownLatch), produces);
        startConsumers(cyclicBarrier, Optional.of(countDownLatch), consumers);
    }

    private void startProducers(CyclicBarrier cyclicBarrier, Optional<CountDownLatch> countDownLatch, int producers) {
        IntStream.
            range(0, producers).
            forEach(value ->
                cachedThreadPool.execute  ((() -> process(cyclicBarrier, countDownLatch, value, producedMsg,  consumedMsg, HIGH_BORER, ()->counterManagerService.increase())
            ))
        );
    }

    private void startConsumers(CyclicBarrier cyclicBarrier, Optional<CountDownLatch> countDownLatch, int consumers) {
        IntStream.
            range(0, consumers).
            forEach(value ->
                cachedThreadPool.execute(() -> process(cyclicBarrier,countDownLatch, value,  consumedMsg,producedMsg, LOW_BORER, ()->counterManagerService.decrease())
                )
            );

    }

    @Override
    public void awake() {
        cachedThreadPool.execute(() -> {
            try {
                producersLock.lock();
                producedMsg.signalAll();
                consumedMsg.signalAll();
            } finally {
                producersLock.unlock();
            }
        });
    }

    private void process(CyclicBarrier cyclicBarrier, Optional<CountDownLatch> countDownLatch,  Integer threadId, Condition from, Condition to, int border, Runnable valueChanger) {
        waitBarrier(cyclicBarrier);
        try {
            producersLock.lock();
            while (counterManagerService.getValue() == border) {
                if (!borderReached) {
                    borderReached = true;
                    System.out.println("border");
                    borderInfoService.persist(new BorderInfoEntity(Counter.getInstance().getValue()));
                }
                from.await();
            }
            valueChanger.run();
            System.out.println(String.format("%s-%s count=%s",from.equals(producedMsg)?"producers":"consumers" ,threadId, counterManagerService.getValue()));
            borderReached = false;
            to.signal();
        } catch (InterruptedException e) {
            throw new  RuntimeException(e);
        } finally {
            if (countDownLatch.isPresent()) {
                countDownLatch.get().countDown();
            }
            producersLock.unlock();
        }
    }

    private void waitBarrier(CyclicBarrier cyclicBarrier) {
        try {
            cyclicBarrier.await();
        } catch (InterruptedException| BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
