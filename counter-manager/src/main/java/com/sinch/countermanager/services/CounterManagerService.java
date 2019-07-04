package com.sinch.countermanager.services;

import com.sinch.countermanager.model.CounterEntity;

public interface CounterManagerService {
    void increase();
    void decrease();
    void setValue(Integer value);
    Integer getValue();
    void persist(CounterEntity counterEntity);
    void load(Integer id);
}
