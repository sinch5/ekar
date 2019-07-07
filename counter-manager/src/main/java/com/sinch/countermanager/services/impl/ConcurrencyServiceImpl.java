package com.sinch.countermanager.services.impl;

import com.sinch.countermanager.model.BorderInfoEntity;
import com.sinch.countermanager.services.BorderInfoService;
import com.sinch.countermanager.services.ConcurrencyService;
import com.sinch.countermanager.services.CounterManagerService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

/**
 * @author Sinchinov Yury
 * This service start producers and consumers to change concurrently counter value
 */
@Service("concurrencyService")
public class ConcurrencyServiceImpl implements ConcurrencyService {
    private interface Command {
        void execute();
    }

    public static final int HIGH_BORER = 100;
    public static final int LOW_BORER = 0;

    private final CounterManagerService counterManagerService;
    private final BorderInfoService borderInfoService;
    private final ExecutorService cachedThreadPool;

    /**
     *
     * @param counterManagerService manager of Counter
     * @param cachedThreadPool pool
     * @param borderInfoService border reaching сщтекщддштп ыукмшсу
     */
    public ConcurrencyServiceImpl(CounterManagerService counterManagerService, ExecutorService cachedThreadPool, BorderInfoService borderInfoService) {
        this.counterManagerService = counterManagerService;
        this.cachedThreadPool = cachedThreadPool;
        this.borderInfoService = borderInfoService;
    }

    @Override
    public void start(int producers, int consumers) {
        startProducers(producers, Optional.empty());
        startConsumers(consumers, Optional.empty());
    }

    @Override
    public void start(int produces, int consumers, CountDownLatch countDownLatch) {
        startProducers(produces, Optional.of(countDownLatch));
        startConsumers(consumers, Optional.of(countDownLatch));
    }

    /**
     *  It needs for waking threads in case counter is set through endpoint
     */
    @Override
    public void awake() {
        synchronized (counterManagerService) {
            counterManagerService.notifyAll();
        }
    }

    private void startProducers(int producers, Optional<CountDownLatch> countDownLatch) {
        IntStream.
            range(0, producers).
            forEach(value -> runThreads("producer", value,  HIGH_BORER, ()->counterManagerService.increase(), countDownLatch));
    }

    private void startConsumers(int consumers,  Optional<CountDownLatch> countDownLatch) {
        IntStream.
            range(0, consumers).
            forEach(value -> runThreads( "consumer",value, LOW_BORER, ()->counterManagerService.decrease(), countDownLatch));
    }

    private void runThreads(String threadType, int value, int border, Command command, Optional<CountDownLatch> countDownLatch) {
        cachedThreadPool.execute(() -> process(threadType, value,  border, ()->command.execute(), countDownLatch));
    }

    private void process(String threadType, int threadId, int border, Command valueChanger, Optional<CountDownLatch> countDownLatch) {
        synchronized (counterManagerService) {
            while (isBorderReached(border)) {
                waitWhenDecreased();
            }

            valueChanger.execute();
            logChanging(threadType, threadId);
            fixBorderReached(border, threadId);

            if (countDownLatch.isPresent()) {
                countDownLatch.get().countDown();
            }
            counterManagerService.notify();
        }
    }

    private void waitWhenDecreased() {
        try {
            counterManagerService.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void logChanging(String name, int threadId) {
        System.out.println(String.format("%s-%s count=%s",name, threadId, counterManagerService.getValue()));
    }

    void fixBorderReached(int border, Integer threadId) {
        if (isBorderReached(border)) {
            borderInfoService.persist(new BorderInfoEntity(border, threadId));
        }
    }

    boolean isBorderReached(int border) {
        return counterManagerService.getValue() == border;
    }
}
