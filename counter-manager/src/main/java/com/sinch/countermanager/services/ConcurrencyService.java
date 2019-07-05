package com.sinch.countermanager.services;

import java.util.concurrent.CountDownLatch;

public interface ConcurrencyService {
    void start(int producers, int consumers);
    void start(int producers, int consumers, CountDownLatch countDownLatch);
    void awake();
}
