package com.sinch.countermanager.services.impl;

/**
 * Singleton stores counter value
 * In case we deal with counter directly through Integer value we will lose reference inside  some threads during setting value
 */
final class Counter {

    private static final Counter counter = new Counter();

    private Integer value = 50;

    private Counter() {
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public static Counter getInstance() {
        return counter;
    }

    public Integer getValue() {
        return value;
    }

    public void increment() {
        value++;
    }

    public void decrement() {
        value--;
    }
}
