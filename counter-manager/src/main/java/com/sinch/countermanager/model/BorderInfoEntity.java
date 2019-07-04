package com.sinch.countermanager.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class BorderInfoEntity {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime timestamp;

    private Integer value;

    public BorderInfoEntity(Integer value) {
        this.value = value;
        timestamp = LocalDateTime.now();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Integer getValue() {
        return value;
    }
}
