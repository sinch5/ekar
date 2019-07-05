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

/**
 *  End points for dealing with counter
 */
@RestController
public class CounterController {

    private final CounterManagerService counterManagerService;
    private final ConcurrencyService concurrencyService;

    public CounterController(CounterManagerService counterManagerService, ConcurrencyService concurrencyService) {
        this.counterManagerService = counterManagerService;
        this.concurrencyService = concurrencyService;
    }


    @PutMapping("/counter/{value}")
    public void setInitialCount(@PathVariable Integer value) {
        counterManagerService.setValue(value);
        concurrencyService.awake();
    }
}
