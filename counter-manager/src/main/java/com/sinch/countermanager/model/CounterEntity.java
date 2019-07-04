package com.sinch.countermanager.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CounterEntity {

    @Id
    private Integer id;

    private Integer value;

    public CounterEntity() {
    }

    public CounterEntity(Integer value) {
        this.id = 1;
        this.value = value;
    }
}
