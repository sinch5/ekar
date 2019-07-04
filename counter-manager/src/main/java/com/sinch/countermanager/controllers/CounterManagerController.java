package com.sinch.countermanager.controllers;

import com.sinch.countermanager.model.RequestInfoEntity;
import com.sinch.countermanager.services.ConcurrencyService;
import com.sinch.countermanager.services.CounterManagerService;
import com.sinch.countermanager.services.RequestInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CounterManagerController {

    private final RequestInfoService requstInfoService;
    private final CounterManagerService counterManagerService;
    private ConcurrencyService concurrencyService;


    public CounterManagerController(RequestInfoService requstInfoService, CounterManagerService counterManagerService, ConcurrencyService concurrencyService) {
        this.requstInfoService = requstInfoService;
        this.counterManagerService = counterManagerService;
        this.concurrencyService = concurrencyService;
    }

    @PutMapping("/producers/{producers}/consumers/{consumers}")
    public ResponseEntity<RequestInfoEntity> setProducerAndConsumerCount(@PathVariable Integer producers, @PathVariable Integer consumers) {
        RequestInfoEntity requestInfoEntity = new RequestInfoEntity(producers, consumers);

        concurrencyService.start(requestInfoEntity.getProducers(), requestInfoEntity.getConsumers());
        requstInfoService.persist(requestInfoEntity);

        return new ResponseEntity(requestInfoEntity, HttpStatus.CREATED);
    }

    @PutMapping("/counter/{value}")
    void setInitialCount(@PathVariable Integer value) {
        counterManagerService.setInitialValue(value);
        concurrencyService.awake();
    }

}
