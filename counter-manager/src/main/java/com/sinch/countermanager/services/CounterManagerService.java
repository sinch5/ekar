package com.sinch.countermanager.services;

public interface CounterManagerService {
    void increase();
    void decrease();
    void setValue(Integer value);
    Integer getValue();
}
