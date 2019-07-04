package com.sinch.countermanager.services.impl;

import com.sinch.countermanager.model.CounterEntity;
import com.sinch.countermanager.repository.CounterValueRepository;
import com.sinch.countermanager.services.CounterManagerService;
import org.springframework.stereotype.Service;

/**
 * Used for control of counter value
 *
 *  Non Thread safe class.
 *  Should be synchronized externally
 */
@Service("counterManagerService")
public class CounterManagerServiceImpl implements CounterManagerService {

    private final CounterValueRepository counterValueRepository;

    public CounterManagerServiceImpl(CounterValueRepository counterValueRepository) {
        this.counterValueRepository = counterValueRepository;
    }

    @Override
    public void increase() {
        Counter.getInstance().increment();
    }

    @Override
    public void decrease() {
        Counter.getInstance().decrement();
    }

    @Override
    public void setInitialValue(Integer value) {
        Counter.getInstance().setValue(value);
        counterValueRepository.save(new CounterEntity(value));
    }

    @Override
    public Integer getValue() {
        return Counter.getInstance().getValue();
    }

    @Override
    public void persist(CounterEntity counterEntity) {
        counterValueRepository.save(counterEntity);
    }

    @Override
    public void load(Integer id) {
        counterValueRepository.getOne(id);
    }
}
