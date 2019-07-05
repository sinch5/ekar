package com.sinch.countermanager.services.impl;

import com.sinch.countermanager.model.CounterEntity;
import com.sinch.countermanager.repository.CounterValueRepository;
import com.sinch.countermanager.services.CounterManagerService;
import org.springframework.stereotype.Service;

import static com.sinch.countermanager.services.impl.ConcurrencyServiceImpl.HIGH_BORER;
import static com.sinch.countermanager.services.impl.ConcurrencyServiceImpl.LOW_BORER;

/**
 * @author Sinchinov Yury
 *
 *  Used for control of counter value
 *
 *  Non Thread safe class.
 *  Should be synchronized externally
 */
@Service("counterManagerService")
public class CounterManagerServiceImpl implements CounterManagerService {

    private final CounterValueRepository counterValueRepository;

    private Integer counter = 50;

    public CounterManagerServiceImpl(CounterValueRepository counterValueRepository) {
        this.counterValueRepository = counterValueRepository;
    }

    @Override
    public void increase() {
        counter++;
    }

    @Override
    public void decrease() {
        counter--;
    }

    @Override
    public void setValue(Integer value) {
        if (value > HIGH_BORER || value < LOW_BORER) {//to convey invariant
           throw new RuntimeException("Inaccessible value");
        }
        counter = value;
        System.out.println("counter is set to " + value);
        counterValueRepository.save(new CounterEntity(value));
    }

    @Override
    public Integer getValue() {
        return counter;
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
