package com.sinch.countermanager.controllers;

import com.sinch.countermanager.model.RequestInfoEntity;
import com.sinch.countermanager.services.ConcurrencyService;
import com.sinch.countermanager.services.RequestInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  End points for dealing with producer and consumer
 */
@RestController
public class ProducerConsumerController {

    private final RequestInfoService requstInfoService;
    private ConcurrencyService concurrencyService;

    public ProducerConsumerController(RequestInfoService requstInfoService, ConcurrencyService concurrencyService) {
        this.requstInfoService = requstInfoService;
        this.concurrencyService = concurrencyService;
    }

    @PutMapping("/producers/{producers}/consumers/{consumers}")
    public ResponseEntity<RequestInfoEntity> setProducerAndConsumerCount(@PathVariable Integer producers, @PathVariable Integer consumers) {
        RequestInfoEntity requestInfoEntity = new RequestInfoEntity(producers, consumers);

        concurrencyService.start(requestInfoEntity.getProducers(), requestInfoEntity.getConsumers());
        requstInfoService.persist(requestInfoEntity);

        return new ResponseEntity(requestInfoEntity, HttpStatus.CREATED);
    }

}
