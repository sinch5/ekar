package com.sinch.countermanager.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public final class RequestInfoEntity {

    @Id
    @GeneratedValue
    private Long id;

    private final Integer producers;
    private final Integer consumers;
    private LocalDateTime timestamp;

    public RequestInfoEntity(Integer producers, Integer consumers) {
        this.producers = producers;
        this.consumers = consumers;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Integer getProducers() {
        return producers;
    }

    public Integer getConsumers() {
        return consumers;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
