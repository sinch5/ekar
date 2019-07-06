package com.sinch.countermanager.services.impl;

import com.sinch.countermanager.model.BorderInfoEntity;
import com.sinch.countermanager.services.BorderInfoService;
import com.sinch.countermanager.services.ConcurrencyService;
import com.sinch.countermanager.services.CounterManagerService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
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
    private interface Command {
        void execute();
    }

    public static final int HIGH_BORER = 100;
    public static final int LOW_BORER = 0;

    private final CounterManagerService counterManagerService;
    private final BorderInfoService borderInfoService;
    private final ExecutorService cachedThreadPool;

    private final ReentrantLock producersLock =  new ReentrantLock(true);

    private final Condition producedMsg = producersLock.newCondition();
    private final Condition consumedMsg = producersLock.newCondition();

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

    private void startProducers(int producers, Optional<CountDownLatch> countDownLatch) {
        IntStream.
            range(0, producers).
            forEach(value -> runThreads(countDownLatch, value, producedMsg, consumedMsg, HIGH_BORER, ()->counterManagerService.increase()));
    }

    private void startConsumers(int consumers,  Optional<CountDownLatch> countDownLatch) {
        IntStream.
            range(0, consumers).
            forEach(value -> runThreads(countDownLatch, value,  consumedMsg, producedMsg, LOW_BORER, ()->counterManagerService.decrease()));
    }

    private void runThreads(Optional<CountDownLatch> countDownLatch, int value, Condition producedMsg, Condition consumedMsg, int highBorer, Command command) {
        cachedThreadPool.execute(() -> process(countDownLatch, value, producedMsg,  consumedMsg, highBorer, ()->command.execute()));
    }

    private void process(Optional<CountDownLatch> countDownLatch,  Integer threadId, Condition from, Condition to, int border, Runnable valueChanger) {
        try {
            producersLock.lock();
            while (counterManagerService.getValue() == border) {
                from.await();
            }
            valueChanger.run();
            logChanging(from, threadId);
            fixBorderReached(border);
            to.signal();//Inform producer or consumer that counter is changed
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (countDownLatch.isPresent()) {
                countDownLatch.get().countDown();
            }
            producersLock.unlock();
        }
    }

    private void logChanging(final Condition from, int threadId) {
        System.out.println(String.format("%s-%s count=%s", from.equals(producedMsg)?"producer":"consumer", threadId, counterManagerService.getValue()));
    }

    void fixBorderReached(int border) {
        if (counterManagerService.getValue() == border) {
            borderInfoService.persist(new BorderInfoEntity(border));
        }
    }
}
